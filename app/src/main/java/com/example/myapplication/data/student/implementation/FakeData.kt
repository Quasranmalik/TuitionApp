package com.example.myapplication.data.student.implementation

import com.example.myapplication.ui.model.PaymentMonth
import java.time.Month
import java.time.YearMonth

val month1 =  PaymentMonth(fee=100,day = 1, month= YearMonth.of(2023,Month.JANUARY))
val month2 = month1.copy(month=month1.month.plusMonths(1))
val month3 = month1.copy(month=month1.month.plusMonths(2))
val month4 = month1.copy(month=month1.month.plusMonths(3))
val month5 = month1.copy(month=month1.month.plusMonths(4))
val month6 = month1.copy(month=month1.month.plusMonths(5))
val month7 = month1.copy(month=month1.month.plusMonths(6))
val month8 = month1.copy(month=month1.month.plusMonths(7))
val month9 = month1.copy(month=month1.month.plusMonths(8))
val month10 = month1.copy(month=month1.month.plusMonths(9))
val month11 = month1.copy(month=month1.month.plusMonths(10))
val month12 = month1.copy(month=month1.month.plusMonths(11))

val dueMonths = listOf(month1,month2, month3, month4, month5,month6)

val advanceMonths = listOf(month7,month8,month9,month10,month11,month12)