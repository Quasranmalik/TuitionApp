package com.example.myapplication.ui.payment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.Period
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class PaymentViewModel {

    private val lastPaidDate = LocalDate.of(2023,6,3)

    private val _paymentUiState = MutablePaymentUiState("name",lastPaidDate)

    val paymentUiState : PaymentUiState = _paymentUiState

    fun onSelectedMonthChange (index:Int){

       with(_paymentUiState){
           if (selectedTill < dueMonths.size && selectedTill==index){selectedTill -= 1 }
           else {selectedTill=index}

           val deleteFrom = selectedTill - dueMonths.size
           if(deleteFrom >= 0) advanceMonths.removeRange(deleteFrom,advanceMonths.lastIndex) else advanceMonths.clear()


       }


    }

    fun pay(){}


    fun advance(){
        _paymentUiState.advanceMonths.add(PaymentMonth(YearMonth.now(),500))
    }

}

 interface PaymentUiState {
     val name:String
     val dueMonths: List<PaymentMonth>
     val advanceMonths: List<PaymentMonth>
     val selectedTill:Int
 }

private class  MutablePaymentUiState (override val name:String,
                              lastPaidDate:LocalDate
):PaymentUiState{

     override val dueMonths = getDueMonths(lastPaidDate,LocalDate.now())
     override  val advanceMonths = mutableStateListOf<PaymentMonth>()
//    override  val advanceMonths = List(7){ PaymentMonth(YearMonth.now(),500) }.toMutableStateList()

     override var selectedTill by  mutableIntStateOf(-1)
}

 data class PaymentMonth(private val yearMonth: YearMonth, val fee:Int=500){
     val month
         get()=yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
 }



fun getDueMonths(start: LocalDate,end:LocalDate) =

     with(YearMonth.of(start.year,start.monthValue))
     {List( Period.between(start,end).months){

            PaymentMonth(yearMonth = this.plusMonths(it.toLong()),fee=500)

        }
    }
