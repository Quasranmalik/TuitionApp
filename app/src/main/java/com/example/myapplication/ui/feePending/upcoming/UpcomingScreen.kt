package com.example.myapplication.ui.feePending.upcoming

import androidx.annotation.RestrictTo
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.example.myapplication.ui.components.Student
import com.example.myapplication.ui.components.StudentList
import com.example.myapplication.ui.feePending.DayChanger
import com.example.myapplication.ui.feePending.FeePendingSections
import com.example.myapplication.ui.feePending.FeePendingTopAppBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun UpcomingScreen(
    students: Flow<PagingData<UpcomingStudent>>,
    getUpcomingDays: () -> Int,
    onUpcomingDaysChange: (Int) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    navigateToPayment: (studentId: Long) -> Unit,
    getPendingAmount: () -> Int,
    retrievePendingAmount: (studentId: Long) -> Unit
) {

    Scaffold (
        topBar = {
            FeePendingTopAppBar(tabs = FeePendingSections.entries.toTypedArray(),
                currentRoute = FeePendingSections.Upcoming.route,
                navigateToRoute = onNavigateToRoute ,
                action = { DayChanger(day = getUpcomingDays(),
                    onDayChange = onUpcomingDaysChange) })
        }
    ){contentPadding ->
        UpcomingStudentList(
            contentPadding=contentPadding,
            students = students,
            getPendingAmount = getPendingAmount,
            retrievePendingAmount = retrievePendingAmount,
            onPay = navigateToPayment
            )
    }
    
}

@Composable
fun UpcomingStudentList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    students: Flow<PagingData<UpcomingStudent>>,
    getPendingAmount: () -> Int,
    retrievePendingAmount: (studentId: Long) -> Unit,
    onPay: (studentId: Long) -> Unit
) {
    StudentList(modifier=modifier,
        contentPadding=contentPadding,
    students = students) {student,expanded,onExpandToggle ->

        UpcomingStudent(
            expanded = expanded,
            student = student,
            retrievePendingAmount = retrievePendingAmount,
            getPendingAmount = getPendingAmount,
            onExpandToggle ={onExpandToggle()},
            onPay = onPay
        )
        
    }
    
}

@Composable
fun UpcomingStudent(modifier: Modifier = Modifier,
                    expanded:Boolean,
                    student:UpcomingStudent,
                    retrievePendingAmount: (studentId: Long) -> Unit,
                    getPendingAmount: () -> Int,
                    onExpandToggle:(Boolean)->Unit,
                    onPay:(studentId:Long)->Unit
                    ) {
    Student(modifier=modifier,
        expanded = expanded,
        name = student.name,
        pendingMonths = student.pendingMonths,
        upcomingDays = student.upcomingDays,
        pendingAmount = getPendingAmount(),
        onPay = { onPay(student.id) },
        onExpandToggle = { expanded ->
            if (expanded) retrievePendingAmount(student.id)
            onExpandToggle(expanded)
        })
}

@RestrictTo(RestrictTo.Scope.TESTS)
val mockStudents = MutableStateFlow(
    PagingData.from(listOf(
        UpcomingStudent(id =1,name ="Apple", pendingMonths = 1, upcomingDays = 4),
        UpcomingStudent(id =2,name ="Banana", pendingMonths = 0, upcomingDays = 3),
        UpcomingStudent(id=3,name ="PineApple", pendingMonths = 5, upcomingDays = 2)
        )
    )
)


@Preview
@Composable
private fun UpcomingScreenPreview() {
    UpcomingScreen(
        students = mockStudents,
        getUpcomingDays = { 5 },
        onUpcomingDaysChange = {},
        onNavigateToRoute = {},
        navigateToPayment = {},
        getPendingAmount = { 300 },
        retrievePendingAmount = {})
}