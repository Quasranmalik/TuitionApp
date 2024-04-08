package com.example.myapplication.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.myapplication.data.student.StudentRepository
import com.example.myapplication.model.NameWithPendingMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

private const val SORT_FIELD_SAVED_STATE_KEY = "SortFieldKey"
private const val ASCENDING_SAVED_STATE_KEY = "AscendingKey"

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val savedStateHandle:SavedStateHandle,
    private val studentRepository: StudentRepository):ViewModel() {



    private var sortField :StateFlow<SortField> = savedStateHandle.getStateFlow(
        key= SORT_FIELD_SAVED_STATE_KEY,initialValue = SortField.Name
    )

   private var ascending :StateFlow<Boolean> = savedStateHandle.getStateFlow(
   key= ASCENDING_SAVED_STATE_KEY,initialValue = true)



    @OptIn(ExperimentalCoroutinesApi::class)
    val students = combine(
        sortField,
        ascending
    ){sortField, ascending->
        studentRepository.allStudentWithPendingMonths(10,sortField,ascending)

    }.flattenConcat() .map {studentPagingData ->
        studentPagingData.map {
            NameWithPendingMonth.StudentItem(id=it.id,
                name=it.firstName+it.lastName,
                classYear = it.classYear,
                pendingMonths = it.pendingMonths + Period.between(it.feeDate, LocalDate.now()).months)

        }.insertSeparators { before, after ->
            val description:String? = when(sortField.value){
                SortField.Name -> when{
                    before == null -> after?.name?.take(1)?.uppercase()
                    before.name.first() != after?.name?.first() -> after?.name?.take(1)?.uppercase()
                    else -> null
                }
                SortField.Class -> when{
                    before == null -> after?.classYear.toString()
                    before.classYear != after?.classYear -> after?.classYear.toString()
                    else -> null
                }

            }
            description?.let { NameWithPendingMonth.SeparatorItem(it) }

        }

    }.cachedIn(viewModelScope)



    fun onSortChange(selectedSortField: SortField){
        if (selectedSortField == sortField.value) savedStateHandle[ASCENDING_SAVED_STATE_KEY] = !ascending.value
        else {savedStateHandle[SORT_FIELD_SAVED_STATE_KEY] = selectedSortField
            savedStateHandle[ASCENDING_SAVED_STATE_KEY]=true}
    }
}


enum class SortField  { Name,Class }