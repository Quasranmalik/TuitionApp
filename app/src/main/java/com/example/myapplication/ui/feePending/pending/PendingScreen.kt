package com.example.myapplication.ui.feePending.pending

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
import com.example.myapplication.ui.feePending.FeePendingSections
import com.example.myapplication.ui.feePending.FeePendingTopAppBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PendingScreen(
    students: Flow<PagingData<PendingStudent>>,
    retrievePendingAmount: (studentId: Long) -> Unit,
    getPendingAmount: () -> Int,
    onNavigateToRoute: (String) -> Unit,
    navigateToPayment: (studentId: Long) -> Unit
) {

    Scaffold (
        topBar = {
            FeePendingTopAppBar(tabs = FeePendingSections.entries.toTypedArray(),
                currentRoute = FeePendingSections.Pending.route,
                navigateToRoute = onNavigateToRoute )
        }
    ){contentPadding ->
        PendingStudentList(
            contentPadding=contentPadding,
            students = students,
            getPendingAmount = getPendingAmount,
            retrievePendingAmount = retrievePendingAmount,
            onPay=navigateToPayment)
    }


}


@Composable
fun PendingStudentList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    students: Flow<PagingData<PendingStudent>>,
    getPendingAmount: () -> Int,
    retrievePendingAmount: (studentId: Long) -> Unit,
    onPay: (studentId: Long) -> Unit
) {

    StudentList(modifier=modifier,
        contentPadding=contentPadding,
        students = students ) {student,expanded ,onExpandToggle ->
        PendingStudent(
            expanded = expanded,
            student = student ,
            retrievePendingAmount = retrievePendingAmount,
            getPendingAmount = getPendingAmount,
            onExpandToggle = {onExpandToggle()},
            onPay = onPay
        )

    }
}


@Composable
fun PendingStudent(modifier: Modifier=Modifier, expanded:Boolean, student: PendingStudent,
                   retrievePendingAmount:(studentId:Long)->Unit,getPendingAmount:()->Int,
                   onExpandToggle:(Boolean)->Unit,onPay:(studentId:Long)->Unit) {
    
    
    Student(modifier=modifier,
        expanded = expanded,
        name = student.name,
        pendingMonths =student.pendingMonths ,
        pendingAmount = getPendingAmount(),
        onPay = { onPay(student.id) },
        onExpandToggle = {expanded ->
            if(expanded) retrievePendingAmount(student.id)
            onExpandToggle(expanded)
        }
    )
}
@RestrictTo(RestrictTo.Scope.TESTS)
@Preview
@Composable
fun PendingStudentListPreview() {

    PendingStudentList(
        students = students,
        getPendingAmount = { 300 },
        retrievePendingAmount = {},
        onPay = {}
    )
}

@RestrictTo(RestrictTo.Scope.TESTS)
val students = MutableStateFlow(PagingData.from(listOf(
    PendingStudent(id=1,name = "Apple",pendingMonths=4),
    PendingStudent(id=2,name="Banana",pendingMonths =0),
    PendingStudent(id=3,name = "PineApple",pendingMonths =1)
)))