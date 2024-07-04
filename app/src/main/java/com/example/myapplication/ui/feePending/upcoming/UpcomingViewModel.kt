package com.example.myapplication.ui.feePending.upcoming

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.myapplication.data.student.StudentRepository
import com.example.myapplication.ui.util.monthsBetween
import com.example.myapplication.ui.util.upcomingDays
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class UpcomingStudent(val id:Long,val name:String,
                           val pendingMonths:Int,val upcomingDays:Int)
private const val UPCOMING_DAYS_SAVED_STATE_KEY = "UpcomingDaysKey"


@HiltViewModel
class UpcomingViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle,
    private val studentRepository: StudentRepository): ViewModel() {

    val upcomingDays : StateFlow<Int> = savedStateHandle.getStateFlow(
        key= UPCOMING_DAYS_SAVED_STATE_KEY, initialValue = 5)

    var pendingAmount by mutableIntStateOf(0)
        private set

    @OptIn(ExperimentalCoroutinesApi::class)
    val students = upcomingDays.flatMapLatest { upcomingDays ->
        studentRepository.upcomingStudents(pageSize = 10,days = upcomingDays)
    }.map {studentPagingData ->

        studentPagingData.map {
            UpcomingStudent(
                id = it.id,
                name =it.firstName + it.lastName,
                pendingMonths =it.pendingMonths + monthsBetween(it.feeDate , LocalDate.now()),
                upcomingDays = upcomingDays(feeDate=it.feeDate)
            )
        }

    }.cachedIn(viewModelScope)


    fun onChangeUpcomingDays(days:Int){
        savedStateHandle[UPCOMING_DAYS_SAVED_STATE_KEY] = days
    }

    fun retrievePendingAmount(studentId:Long){
        viewModelScope.launch {
            pendingAmount = studentRepository.getPendingAmount(studentId)
        }
    }



}