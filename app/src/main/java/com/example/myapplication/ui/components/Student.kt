package com.example.myapplication.ui.components

import androidx.annotation.RestrictTo
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.modifiers.circleBackground


//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun DeletableStudentList(modifier: Modifier=Modifier, studentPagingDataFlow: Flow<PagingData<HomeUiModel.StudentItem>>,
//                         pendingAmount: Int, onPay: (Long) -> Unit, onDelete:(Long) -> Unit) {
//    val studentPagingItems = studentPagingDataFlow.collectAsLazyPagingItems()
//    var expandedAt by remember{ mutableIntStateOf(-1) }
//    var deletingAt by remember{ mutableIntStateOf(-1) }
//    val deleteIcon :@Composable () -> Unit = {Icon(imageVector = Icons.Filled.Delete,contentDescription = null, tint = Color.Red)}
//
//    LazyColumn(modifier=modifier){
//
//        items(count = studentPagingItems.itemCount) { index ->
//
//            val student = studentPagingItems[index]
//            val expanded by remember { derivedStateOf{index == expandedAt} }
//            val draggableState = rememberAnchoredDraggableState()
//            LaunchedEffect(key1 = draggableState.isAnimationRunning){
//                deletingAt=index
//            }
//            LaunchedEffect(key1=deletingAt){
//                if(deletingAt != index) draggableState.snapTo(DragValue.DEFAULT)
//            }
//            SwipeDeleteItem(
//                draggableState = draggableState,
//                deleteIcon = deleteIcon,
//                onDelete = { student?.id?.let { onDelete(it) } }) {
//                student?.let {
//                    HomeStudent(expanded = expanded,
//                        student = it,
//                        pendingAmount = pendingAmount,
//                        onPay = onPay,
//                        onExpandToggle = { expandedAt = if (expandedAt == index) -1
//                        else index})
//                }
//            }
//
//
//        }
//
//    }
//
//
//}

@Composable
fun Student(modifier:Modifier=Modifier, expanded:Boolean,
            name:String,
            pendingMonths:Int,
            pendingAmount: Int,
            upcomingDays: Int? =null,
            onPay:() -> Unit, onExpandToggle:(Boolean)->Unit) {

    val isFeePending = pendingMonths > 0

    Column(modifier=modifier.animateContentSize(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )) {
        ListItem(headlineContent = {StudentName(name)},
            leadingContent = { Icon(Icons.Default.Person, contentDescription = "Student") },
            trailingContent = {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    upcomingDays?.let { UpcomingDaysBadge(upcomingDays = it) }


                    Spacer(Modifier.width(15.dp))
                    if (isFeePending) {
                        PendingMonthBadge(pendingMonths)
                        DropDownIcon(expanded = expanded, onToggle = onExpandToggle)
                    }

                }


            }
        )

        if (expanded) {
            PendingFeeCard(
                pendingAmount = pendingAmount,
                onPay = onPay
            )

        }
    }

}

@Composable
fun PendingFeeCard(pendingAmount:Int,onPay:()->Unit) {
    Card {
        Row (modifier= Modifier
            .fillMaxWidth()
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .alignByBaseline(),
                text = "₹$pendingAmount",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.weight(1.0f))
            Button(onClick = onPay) {
                Text(text="Pay",style= MaterialTheme.typography.titleLarge)

            }

        }
    }
}

@Composable
fun StudentName(name:String) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleLarge
    )
}
@Composable
fun PendingMonthBadge(pendingMonths: Int,modifier:Modifier=Modifier) {
    Text(
        modifier = modifier.circleBackground(color = Color.Red, padding = 1.dp),
        text = "$pendingMonths",
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun UpcomingDaysBadge(modifier:Modifier=Modifier,upcomingDays:Int) {
    Text(
        modifier=modifier,
        text = "$upcomingDays",
        style = MaterialTheme.typography.bodyLarge
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

@Preview
@Composable
fun StudentPreview() {
    var expanded by remember{mutableStateOf(true)}
    Student(
        expanded  = expanded,
        name = "Abcd",
        pendingMonths = 3,
        upcomingDays = 3,
        pendingAmount = 300,
        onPay = {},
        onExpandToggle = {expanded = !expanded}

    )
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
    PendingMonthBadge(3)
}







//@Preview
@Composable
fun PendingFeeCardPreview() {
    PendingFeeCard(pendingAmount = 600, onPay = {})
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
