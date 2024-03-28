package com.example.myapplication.model

import androidx.annotation.RestrictTo
import androidx.compose.runtime.Stable

@Stable
 sealed class NameWithPendingMonth{
     class StudentItem(val id:Long, val name:String, val classYear:Int=1,
                       val pendingMonths:Int):NameWithPendingMonth()
     class SeparatorItem(val description:String):NameWithPendingMonth()
 }

@RestrictTo(RestrictTo.Scope.TESTS)
val student1 = NameWithPendingMonth.StudentItem(id = 1, name = "Quasran Malik", pendingMonths = 4)

@RestrictTo(RestrictTo.Scope.TESTS)
val students = listOf(
    student1,
    NameWithPendingMonth.StudentItem(id=2,name="Apple", pendingMonths = 1),
    NameWithPendingMonth.StudentItem(id=2,name="Mango", pendingMonths = 3),
    NameWithPendingMonth.StudentItem(id=3,name="Banana", pendingMonths = 1),
    NameWithPendingMonth.StudentItem(id=4,name="PineApple", pendingMonths = 0)
)
