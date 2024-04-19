package com.example.myapplication.ui.home

import androidx.annotation.RestrictTo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myapplication.ui.components.Student
import com.example.myapplication.ui.home.model.HomeUiModel
import com.example.myapplication.ui.home.model.SortField
import com.example.myapplication.ui.home.model.students
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
internal fun HomeScreen(studentList: Flow<PagingData<HomeUiModel>>,
               pendingAmount:Int,
               sortField: SortField,
               onSortChange:(SortField) ->Unit,
                onPay: (studentId: Long) -> Unit,
                getPaymentAmount: (studentId: Long) -> Unit
                        ) {
    Scaffold(
        topBar = {HomeTopAppBar(sortField = sortField, onSortChange = onSortChange)}
    ) {paddingValue->
        HomeStudentList(studentPagingDataFlow = studentList ,
            contentPadding = paddingValue,
            pendingAmount = pendingAmount ,
            onPay = onPay ,
            getPaymentAmount = getPaymentAmount)

    }

}










@Composable
fun HomeStudentList(modifier: Modifier = Modifier,
                    contentPadding: PaddingValues = PaddingValues(0.dp),
                    studentPagingDataFlow: Flow<PagingData<HomeUiModel>>,
                    pendingAmount: Int,
                    onPay: (studentId:Long) -> Unit,
                    getPaymentAmount:(studentId:Long) -> Unit) {

    val studentPagingItems = studentPagingDataFlow.collectAsLazyPagingItems()
    var expandedAt by remember { mutableIntStateOf(-1) }

    LazyColumn(modifier = modifier,contentPadding = contentPadding) {
        for(index in 0  until studentPagingItems.itemCount) {
            when (val student = studentPagingItems.peek(index)) {
                is HomeUiModel.StudentItem -> item {
                    val expanded by remember { derivedStateOf { index == expandedAt } }
                    HomeStudent(
                        expanded = expanded,
                        student = student,
                        pendingAmount = pendingAmount,
                        onPay = onPay,
                        onExpandToggle = {
                            expandedAt = if (expandedAt == index) -1
                            else index
                        },
                        getPaymentAmount = getPaymentAmount
                    )
                }

                is HomeUiModel.SeparatorItem ->  StudentSeparator(student)
               else -> null
            }


        }

        if (studentPagingItems.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun HomeStudent(modifier: Modifier = Modifier, expanded:Boolean, student: HomeUiModel.StudentItem,
                pendingAmount: Int,
                onPay:(id:Long) -> Unit, onExpandToggle:(expanded:Boolean)->Unit,
                getPaymentAmount: (studentId: Long) -> Unit) {



    Student(
        modifier =modifier,
        expanded = expanded,
        name = student.name,
        pendingMonths = student.pendingMonths,
        pendingAmount = pendingAmount,
        onPay = {onPay(student.id)},
        onExpandToggle = {if (!it) getPaymentAmount(student.id)
        onExpandToggle(it)}

    )





}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.StudentSeparator(separator:HomeUiModel.SeparatorItem) {
    stickyHeader {
        Box(modifier= Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary),
            contentAlignment = Alignment.CenterStart){
            Text(text=separator.description,style=MaterialTheme.typography.titleMedium,
                color=MaterialTheme.colorScheme.onTertiary)
        }
    }
}


@Preview
@RestrictTo(RestrictTo.Scope.TESTS)
@Composable
fun HomeScreenPreview() {
    HomeScreen(studentList = remember { MutableStateFlow(PagingData.from(students)) },
        pendingAmount = 100, sortField = SortField.Name, onSortChange = {},onPay={} , getPaymentAmount = {})
}
@Preview
@RestrictTo(RestrictTo.Scope.TESTS)
@Composable
fun HomeStudentListPreview() {
    val studentFlow= remember { MutableStateFlow(PagingData.from(students)) }
    HomeStudentList(studentPagingDataFlow = studentFlow,
        pendingAmount = 100,
        onPay = {})
}