package com.example.myapplication.ui.components

import androidx.annotation.RestrictTo
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myapplication.model.NameWithPendingMonth
import com.example.myapplication.model.student1
import com.example.myapplication.ui.modifiers.circleBackground
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun StudentList(modifier: Modifier=Modifier, studentPagingDataFlow: Flow<PagingData<NameWithPendingMonth.StudentItem>>,
                pendingAmount: Int, pendingCalculation: String,onPay: () -> Unit) {

    val studentPagingItems = studentPagingDataFlow.collectAsLazyPagingItems()
    var expandedAt by remember{ mutableIntStateOf(-1) }

    LazyColumn {
        items(count = studentPagingItems.itemCount) { index ->

            val student = studentPagingItems[index]
            val expanded by remember { derivedStateOf{index == expandedAt} }
            student?.let {
                Student(expanded = expanded,
                    student = it,
                    pendingAmount = pendingAmount,
                    pendingCalculation = pendingCalculation,
                    onPay = onPay,
                    onExpandToggle = { expandedAt = if (expandedAt == index) -1
                        else index})
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeletableStudentList(modifier: Modifier=Modifier,studentPagingDataFlow: Flow<PagingData<NameWithPendingMonth.StudentItem>>,
                         pendingAmount: Int, pendingCalculation: String,onPay: () -> Unit,onDelete:(Long) -> Unit) {
    val studentPagingItems = studentPagingDataFlow.collectAsLazyPagingItems()
    var expandedAt by remember{ mutableIntStateOf(-1) }
    var deletingAt by remember{ mutableIntStateOf(-1) }
    val deleteIcon :@Composable () -> Unit = {Icon(imageVector = Icons.Filled.Delete,contentDescription = null, tint = Color.Red)}

    LazyColumn(modifier=modifier){

        items(count = studentPagingItems.itemCount) { index ->

            val student = studentPagingItems[index]
            val expanded by remember { derivedStateOf{index == expandedAt} }
            val draggableState = rememberAnchoredDraggableState()
            LaunchedEffect(key1 = draggableState.isAnimationRunning){
                deletingAt=index
            }
            LaunchedEffect(key1=deletingAt){
                if(deletingAt != index) draggableState.snapTo(DragValue.DEFAULT)
            }
            SwipeDeleteItem(
                draggableState = draggableState,
                deleteIcon = deleteIcon,
                onDelete = { student?.id?.let { onDelete(it) } }) {
                student?.let {
                    Student(expanded = expanded,
                        student = it,
                        pendingAmount = pendingAmount,
                        pendingCalculation = pendingCalculation,
                        onPay = onPay,
                        onExpandToggle = { expandedAt = if (expandedAt == index) -1
                        else index})
                }
            }


        }

    }


}



@Composable
fun Student(modifier:Modifier=Modifier,expanded:Boolean, student: NameWithPendingMonth.StudentItem,
                              pendingAmount: Int,pendingCalculation: String,
                              onPay:() -> Unit,onExpandToggle:(Boolean)->Unit) {

    val isFeePending = student.pendingMonths > 0

    Column(modifier=modifier.animateContentSize(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )) {
        ListItem(headlineContent = {StudentName(student)},
            leadingContent = { Icon(Icons.Default.Person, contentDescription = "Student") },
            trailingContent = {

                if (isFeePending) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        PendingMonthBadge(student)
                        DropDownIcon(expanded = expanded, onToggle = onExpandToggle)
                    }
                }

            }
        )

        if (expanded) {
            PendingFeeCard(
                pendingAmount = pendingAmount,
                pendingCalculation = pendingCalculation,
                onPay = onPay
            )

        }
    }

}

@Composable
fun PendingFeeCard(pendingAmount:Int,pendingCalculation:String,onPay:()->Unit) {
    Card {
        Row (modifier= Modifier
            .fillMaxWidth()
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            Row {
                Text(
                    modifier = Modifier
                        .padding(10.dp)
                        .alignByBaseline(),
                    text = "₹$pendingAmount",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier=Modifier.width(15.dp))
                Text(
                    modifier=Modifier.alignByBaseline(),
                    text = pendingCalculation,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.weight(1.0f))
            Button(onClick = onPay) {
                Text(text="Pay",style= MaterialTheme.typography.titleLarge)

            }

        }
    }
}

@Composable
fun StudentName(student:NameWithPendingMonth.StudentItem) {
    Text(
        text = student.name,
        style = MaterialTheme.typography.titleLarge
    )
}
@Composable
fun PendingMonthBadge(student: NameWithPendingMonth.StudentItem) {
    Text(
        modifier = Modifier.circleBackground(color = Color.Red, padding = 1.dp),
        text = "${student.pendingMonths}",
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun DropDownIcon(expanded: Boolean,onToggle:(Boolean)->Unit) {
    IconButton(onClick = {onToggle(expanded)}) {
        Icon(
            imageVector = when (expanded) {
                true -> Icons.Default.KeyboardArrowUp
                false -> Icons.Default.KeyboardArrowDown
            },
            contentDescription = "dropdown"
        )
    }
}

@RestrictTo(RestrictTo.Scope.TESTS)
//@Preview
@Composable
fun StudentPreview() {
    var expanded by remember{mutableStateOf(true)}
    Student(expanded=expanded ,student=student1, pendingAmount = 600, pendingCalculation = "",onPay={}, onExpandToggle = {expanded=!expanded})
}

//@Preview
@Composable
fun DropDownIconPreview() {
    var expanded by remember{ mutableStateOf(false) }
    DropDownIcon(expanded = expanded, onToggle = {expanded=!expanded})
}

//@Preview
@RestrictTo(RestrictTo.Scope.TESTS)
@Composable
fun PendingMonthBadgePreview() {
    PendingMonthBadge(student = student1)
}







//@Preview
@Composable
fun PendingFeeCardPreview() {
    PendingFeeCard(600,"200x3", onPay = {})
}

//@Preview
//@Composable
//fun SwipeableStudentPreview() {
//    val swipeState = rememberSwipeState()
//
//
//
//    SwipeDeleteItem(swipeState = swipeState,
//        deleteIcon={ Icon(imageVector = Icons.Filled.Delete,contentDescription = null,tint= Color.Red) }){
//        StudentItem(name = "Abcdef")
//    }
//}


//@Preview
@RestrictTo(RestrictTo.Scope.TESTS)
@Composable
fun StudentListPreview() {
    val studentFlow=remember{MutableStateFlow(PagingData.from(List(10){student1}))}
    StudentList(studentPagingDataFlow = studentFlow,
        pendingAmount = 100,
        pendingCalculation = "",onPay={})
}

@Preview
@RestrictTo(RestrictTo.Scope.TESTS)
@Composable
fun DeletableStudentListPreview() {
    val studentFlow=remember{MutableStateFlow(PagingData.from(List(10){student1}))}
    DeletableStudentList(studentPagingDataFlow = studentFlow,
        pendingAmount = 100,
        pendingCalculation = "",onPay={},
        onDelete={})
}