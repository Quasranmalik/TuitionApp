package com.example.myapplication.ui.fee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.student.StudentRepository
import com.example.myapplication.ui.model.PaymentMonth
import com.example.myapplication.ui.util.monthsBetween
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class FeeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val studentRepository: StudentRepository
) : ViewModel(){

    var feeUiState:FeeUiState by mutableStateOf(FeeUiState.Loading)
        private set

    private val studentId:Long = checkNotNull(savedStateHandle["studentId"])


    init{
            loadData()
    }

    private fun loadData(){
        viewModelScope.launch {
            val lastPaidDate = studentRepository.lastPaidDateForStudent(studentId)
            val pendingFeeHistory = studentRepository.pendingFeeHistory(studentId,lastPaidDate)
            val pendingMonths = calculatePendingMonth(lastPaidDate= lastPaidDate,
                pendingFeeHistory = pendingFeeHistory)
            val advanceFeeHistory = studentRepository.advanceFeeHistory(studentId)

            val advanceMonths = calculateAdvanceMonth(
                currentFeeHistory = pendingFeeHistory.lastOrNull(),
                advanceFeeHistory = advanceFeeHistory
            )

            feeUiState = FeeUiState.Success(pendingMonths = pendingMonths,
                advanceMonths = advanceMonths)

        }
    }


}

sealed interface FeeUiState{
    data class Success(val pendingMonths:List<PaymentMonth>,val advanceMonths:List<PaymentMonth>):FeeUiState
    data object Loading :FeeUiState
}

fun calculatePendingMonth(
    lastPaidDate: LocalDate,
    pendingFeeHistory: List<FeeHistory>,
    today: LocalDate = LocalDate.now()):List<PaymentMonth>{

    val pendingMonths:MutableList<PaymentMonth> = mutableListOf()
    var fee = pendingFeeHistory[0].fee
    var day = pendingFeeHistory[0].joinDate.dayOfMonth
    var feeStartDate = lastPaidDate
    var feeEndDate = pendingFeeHistory[1].joinDate
    fun addPaymentMonths(){
        val numberOfMonths = monthsBetween(startDate=feeStartDate,endDate= feeEndDate)
        val month = YearMonth.from(feeStartDate)
        val paymentMonth = PaymentMonth(fee=fee, day=day, month = month)
        repeat(numberOfMonths){
            pendingMonths.add(paymentMonth.copy(month=paymentMonth.month.plusMonths(it.toLong()+1)))
        }
    }

    addPaymentMonths()
    for (index in 1 until pendingFeeHistory.size){
        fee = pendingFeeHistory[index].fee
        if (fee == 0 ) continue
        day = pendingFeeHistory[index].joinDate.dayOfMonth
        feeStartDate = pendingFeeHistory[index].joinDate
        feeEndDate= if (index == pendingFeeHistory.lastIndex){
            today
        }
        else{
            pendingFeeHistory[index+1].joinDate
        }
        addPaymentMonths()
    }

    return pendingMonths



}

fun calculateAdvanceMonth(
    currentFeeHistory:FeeHistory?,
    advanceFeeHistory:List<FeeHistory>,
    today: LocalDate = LocalDate.now()):List<PaymentMonth>{
    val advanceMonths:MutableList<PaymentMonth> = mutableListOf()
    lateinit var feeStartDate:LocalDate
    lateinit var feeEndDate:LocalDate
    var fee:Int =0
    var day:Int=0
    fun addPaymentMonths(){
        val numberOfMonths = monthsBetween(startDate=feeStartDate,endDate= feeEndDate)
        val remainingSpace = 5- advanceMonths.size
        val month = YearMonth.from(feeStartDate)
        val paymentMonth = PaymentMonth(fee=fee, day=day, month = month)
        repeat(if (numberOfMonths > remainingSpace) remainingSpace else numberOfMonths ){
            advanceMonths.add(paymentMonth.copy(month=paymentMonth.month.plusMonths(it.toLong()+1)))
        }
    }

    if(advanceFeeHistory.isEmpty()){
        if (currentFeeHistory == null) return emptyList()
        val currentMonth = YearMonth.from(today)
        val nextAdvanceMonth = if (today.dayOfMonth < currentFeeHistory.joinDate.dayOfMonth) currentMonth
        else currentMonth.plusMonths(1)
        return List(5){
            with(currentFeeHistory){
                PaymentMonth(fee= this.fee,day=this.joinDate.dayOfMonth,month = nextAdvanceMonth.plusMonths(it.toLong()))
            }
        }
    }

    for (index in advanceFeeHistory.indices){
        fee = advanceFeeHistory[index].fee
        day = advanceFeeHistory[index].joinDate.dayOfMonth
        feeStartDate = advanceFeeHistory[index].joinDate
        feeEndDate= if (index == advanceFeeHistory.lastIndex){

            advanceFeeHistory[index].joinDate.plusMonths(5L-advanceMonths.size)
        }
        else{
            advanceFeeHistory[index+1].joinDate
        }
        addPaymentMonths()
        if (advanceMonths.size == 5) break
    }

    return advanceMonths

}
