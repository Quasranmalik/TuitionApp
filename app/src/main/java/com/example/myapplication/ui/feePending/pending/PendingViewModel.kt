package com.example.myapplication.ui.feePending.pending

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.myapplication.data.student.StudentRepository
import com.example.myapplication.ui.util.monthsBetween
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class PendingStudent(val id :Long,val name :String,val pendingMonths:Int)
@HiltViewModel
class PendingViewModel @Inject constructor(private val studentRepository: StudentRepository):ViewModel() {

    var pendingAmount by mutableIntStateOf(0)
        private set

    val students = studentRepository.pendingStudents(10).map {studentPagingData ->
        studentPagingData.map {
            PendingStudent(
                it.id,
                it.firstName + it.lastName,
                it.pendingMonths + monthsBetween(it.feeDate, LocalDate.now())
            )
        }

    }.cachedIn(viewModelScope)

    fun retrievePendingAmount(studentId:Long){
        viewModelScope.launch {
            pendingAmount = studentRepository.getPendingAmount(studentId)
        }
    }

}