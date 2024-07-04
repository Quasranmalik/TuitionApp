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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
internal fun HomeScreen(
    studentList: Flow<PagingData<HomeUiModel>>,
    getPendingAmount: () -> Int,
    sortField: SortField,
    onSortChange:(SortField) ->Unit,
    navigateToPayment: (studentId: Long) -> Unit,
    retrievePendingAmount: (studentId: Long) -> Unit
                        ) {
    Scaffold(
        topBar = {HomeTopAppBar(sortField = sortField, onSortChange = onSortChange)}
    ) {paddingValue->
        HomeStudentList(
            studentPagingDataFlow = studentList ,
            contentPadding = paddingValue,
            getPendingAmount = getPendingAmount ,
            onPay = navigateToPayment,
            retrievePendingAmount = retrievePendingAmount)

    }

}













@Composable
private fun HomeStudentList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    studentPagingDataFlow: Flow<PagingData<HomeUiModel>>,
    getPendingAmount: () -> Int,
    onPay: (studentId:Long) -> Unit,
    retrievePendingAmount:(studentId:Long) -> Unit) {

    val studentPagingItems = studentPagingDataFlow.collectAsLazyPagingItems()
    var expandedAt by remember { mutableIntStateOf(-1) }

    LazyColumn(modifier = modifier,contentPadding = contentPadding) {
        items(count = studentPagingItems.itemCount){index ->

            when (val student = studentPagingItems[index]) {
                is HomeUiModel.StudentItem ->  {
                    val expanded by remember { derivedStateOf { index == expandedAt } }
                    HomeStudent(
                        expanded = expanded,
                        student = student,
                        retrievePendingAmount = retrievePendingAmount,
                        getPendingAmount = getPendingAmount,
                        onExpandToggle = {
                            expandedAt = if (expandedAt == index) -1 else index
                        },
                        onPay = onPay
                    )
                }

                is HomeUiModel.SeparatorItem ->  this@LazyColumn.StudentSeparator(student)
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
private fun HomeStudent(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    student: HomeUiModel.StudentItem,
    retrievePendingAmount: (studentId: Long) -> Unit,
    getPendingAmount: () -> Int,
    onExpandToggle: (expanded: Boolean) -> Unit,
    onPay: (id: Long) -> Unit
) {



    Student(
        modifier =modifier,
        expanded = expanded,
        name = student.name,
        pendingMonths = student.pendingMonths,
        pendingAmount = getPendingAmount(),
        onPay = {onPay(student.id)},
        onExpandToggle = {expanded ->
            if (expanded) retrievePendingAmount(student.id)
        onExpandToggle(expanded)}

    )





}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.StudentSeparator(separator:HomeUiModel.SeparatorItem) {
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
    HomeScreen(
        studentList = remember { MutableStateFlow(PagingData.from(students)) },
        getPendingAmount = {100}, sortField = SortField.Name, onSortChange = {},
        navigateToPayment ={} , retrievePendingAmount = {})
}
@Preview
@RestrictTo(RestrictTo.Scope.TESTS)
@Composable
fun HomeStudentListPreview() {
    val studentFlow= remember { MutableStateFlow(PagingData.from(students)) }
    HomeStudentList(
        studentPagingDataFlow = studentFlow,
        getPendingAmount = {100},
        retrievePendingAmount = {},
        onPay = {})
}