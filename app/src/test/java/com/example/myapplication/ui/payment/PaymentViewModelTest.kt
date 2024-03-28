package com.example.myapplication.ui.payment
//
//import org.assertj.core.api.Assertions.assertThat
//
//import org.junit.Test
//import java.time.LocalDate
//import java.time.Month
//import java.time.Period
//import java.time.format.TextStyle
//import java.util.Locale
//
//class PaymentViewModelTest{
//
//
//    @Test
//    fun checkFunction(){
//        val actual = getDueMonths(LocalDate.of(2023,5,3),LocalDate.now())
//        assertThat(actual).containsExactlyElementsOf(expectedList)
//    }
//
//    private fun getDueMonths(start: LocalDate,end:LocalDate):List<PaymentMonth>{
//        val noOfDueMonths = Period.between(start,end).months
//
//        return List(noOfDueMonths){
//              with( start.plusMonths(it.toLong())){
//                PaymentMonth(this.dayOfMonth,this.month.getDisplayName(TextStyle.FULL,Locale.US),this.year)
//              }
//        }
//    }
//}
//
//
//val expectedList =List(4){PaymentMonth(3,Month.of(5+it).getDisplayName(TextStyle.FULL, Locale.US),2023)}