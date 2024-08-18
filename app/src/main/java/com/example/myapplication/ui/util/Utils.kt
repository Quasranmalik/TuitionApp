package com.example.myapplication.ui.util

import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.ui.model.PaymentMonth
import java.time.LocalDate
import java.time.Period
import java.time.YearMonth

fun monthsBetween(startDate:LocalDate,endDate:LocalDate) = Period.between(startDate,endDate).months

fun upcomingDays(today:LocalDate=LocalDate.now(),feeDate:LocalDate):Int{
    val calculationFeeMonth = if (today.dayOfMonth < feeDate.dayOfMonth) today.monthValue
                            else today.monthValue +1
    val calculationFeeDate = feeDate.withMonth(calculationFeeMonth)
    return Period.between(today,calculationFeeDate).days
}


