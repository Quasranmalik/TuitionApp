package com.example.myapplication.ui.home.model

import androidx.annotation.RestrictTo
import androidx.compose.runtime.Stable

@Stable
 sealed class HomeUiModel{
     data class StudentItem(val id:Long, val name:String, val classYear:Int=1,
                       val pendingMonths:Int): HomeUiModel()
     data class SeparatorItem(val description:String): HomeUiModel()
 }

@RestrictTo(RestrictTo.Scope.TESTS)
val student1 = HomeUiModel.StudentItem(id = 1, name = "Quasran Malik", pendingMonths = 4)

@RestrictTo(RestrictTo.Scope.TESTS)
val students = listOf(
    student1,
    HomeUiModel.SeparatorItem("A"),
    HomeUiModel.StudentItem(id = 2, name = "Apple", pendingMonths = 1),
    HomeUiModel.SeparatorItem("M"),
    HomeUiModel.StudentItem(id = 2, name = "Mango", pendingMonths = 3),
    HomeUiModel.SeparatorItem("B"),
    HomeUiModel.StudentItem(id = 3, name = "Banana", pendingMonths = 1),
    HomeUiModel.SeparatorItem("P"),
    HomeUiModel.StudentItem(id = 4, name = "PineApple", pendingMonths = 0)
)
