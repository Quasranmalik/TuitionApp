package com.example.myapplication.ui.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@Composable
fun PaymentScreen(paymentViewModel: PaymentViewModel,onNavigateBack: () -> Unit) {

    PaymentScreen(
        paymentUiState = paymentViewModel.paymentUiState,
        onSelectedMonthChange = paymentViewModel::onSelectedMonthChange,
        onAdvance = paymentViewModel::advance,
        onPay = paymentViewModel::pay,onNavigateBack=onNavigateBack)
}




@Composable
fun PaymentScreen(paymentUiState: PaymentUiState, onSelectedMonthChange:(Int) -> Unit,onAdvance : () -> Unit, onPay : () -> Unit,onNavigateBack : () -> Unit) {
    val lazyListState = rememberLazyListState()
    val openDialog = remember { mutableStateOf(false) }
    val showAdvanceButton by remember{ derivedStateOf { paymentUiState.selectedTill ==
            paymentUiState.dueMonths.size +  paymentUiState.advanceMonths.size-1 && !lazyListState.canScrollForward} }
    val enablePaymentButton by remember{ derivedStateOf { paymentUiState.selectedTill > -1 }}
    val scrollScope = rememberCoroutineScope()

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {openDialog.value=false},
            title = {
                Text(text = "Payment Confirmation")
            },
            text = {
               PaymentDialogContent()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onPay()
                        onNavigateBack()

                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false

                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }


    Column ( modifier= Modifier
        .padding()
         ){
        Spacer(modifier=Modifier.height(5.dp))
        Surface(modifier= Modifier
            .align(Alignment.End)
            .padding(5.dp),color=MaterialTheme.colorScheme.surfaceVariant) {
            Text(modifier =Modifier.padding(horizontal=2.dp,vertical=4.dp),text= paymentUiState.name,fontSize=20.sp)
        }
        Spacer(modifier=Modifier.height(10.dp))
        PaymentList(
            modifier= Modifier
                .border(1.dp, Color.Black)
                .align(Alignment.CenterHorizontally)
                .height(500.dp)
                .testTag("PaymentList")
                ,
            dueMonths = paymentUiState.dueMonths,
            advanceMonths=paymentUiState.advanceMonths,
            lazyListState = lazyListState,
            selectedTillProvider = { paymentUiState.selectedTill },
            onSelectedMonthChange =onSelectedMonthChange
        )
        AnimatedVisibility(visible = showAdvanceButton,modifier=Modifier.align(Alignment.CenterHorizontally)) {
            FilledTonalButton(onClick = {scrollScope.launch {
                onAdvance()
                lazyListState.animateScrollToItem(with(paymentUiState){dueMonths.size +advanceMonths.size -1})
                }
            }
            ) {
                Icon(imageVector=Icons.Default.Add,contentDescription = "advance")
                Text(text="Advance")

            }
        }

        Box(modifier= Modifier
            .fillMaxWidth()
            .fillMaxHeight())
        {



            Surface(color = MaterialTheme.colorScheme.surfaceVariant,modifier=Modifier.align(Alignment.Center)) {
                Text(modifier=Modifier.padding(5.dp),text = "₹ 500", fontSize = 25.sp)
            }

            Row(modifier= Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 10.dp, vertical = 2.dp)
                .width(200.dp))
            {
                OutlinedButton(onClick = onNavigateBack,modifier=Modifier.weight(1.0f)) {
                    Text(text="Cancel")
                }

                Spacer(modifier=Modifier.width(10.dp))
                Button(onClick = { openDialog.value = true },enabled=enablePaymentButton,modifier=Modifier.weight(1.0f)) {
                    Text(text = "Pay")
                }


            }

        }



    }

    
}

@Composable
fun PaymentList(modifier: Modifier=Modifier,dueMonths:List<PaymentMonth>, advanceMonths:List<PaymentMonth> = emptyList(),lazyListState: LazyListState,
                 feeIncreaseAt:Int=-1,selectedTillProvider: () -> Int, onSelectedMonthChange:(Int) -> Unit) {


    LazyColumn(modifier = modifier
        ,state=lazyListState){


        itemsIndexed(items=dueMonths,
            key = {index, _ -> index }){index, paymentMonth ->
            val checked by remember{ derivedStateOf { index <= selectedTillProvider() }}
            if(index == feeIncreaseAt){ PaymentTextItem(text = "Fee Increase")}

            PaymentItem(checked = checked, onCheckedChange = {_ -> onSelectedMonthChange(index)},
                paymentMonth=paymentMonth)
        }


        itemsIndexed(items=advanceMonths,
            key = {index, _ -> dueMonths.size+ index }){index, paymentMonth ->


            val checked by remember{ derivedStateOf { index+dueMonths.size<= selectedTillProvider() }}

            if(index==0){
                PaymentTextItem(text = "Advance")
                HorizontalDivider()
            }

            if(dueMonths.size+index == feeIncreaseAt){ PaymentTextItem(text = "Fee Increase")}

            PaymentItem(
                checked = checked,
                onCheckedChange = {_ -> onSelectedMonthChange(index+dueMonths.size)} ,
                paymentMonth=paymentMonth
            )

        }



    }
}



//@Preview(showSystemUi = true)
//@Composable
//fun PaymentScreenPreview() {
//    val paymentUiState = remember{TestPaymentUiState(numberOfMonths = 3)}
//    PaymentScreen(paymentUiState =paymentUiState , onSelectedMonthChange = {  paymentUiState.selectedTill=it},onAdvance = {},onPay = {} , onNavigateBack = {})
//
//}

//@Preview
//@Composable
//fun PaymentListPreview() {
//    var selectedTill by remember{ mutableIntStateOf(-1) }
//    val numberOfMonths = remember{4}
//    val lazyListState = rememberLazyListState()
//    val showAdvanceButton by remember{ derivedStateOf { selectedTill == 2*numberOfMonths-1 && !lazyListState.canScrollForward }}
//
//Column{
//    PaymentList(
//        modifier=Modifier,
//        dueMonths = mockMonthList.subList(0,numberOfMonths),
//        lazyListState = lazyListState,
//        advanceMonths = mockMonthList.subList(numberOfMonths,2*numberOfMonths),
//        selectedTillProvider = {selectedTill},
//        feeIncreaseAt = 4,
//        onSelectedMonthChange = {selectedTill=it}
//    )
//    AnimatedVisibility(visible = showAdvanceButton) {
//        Button(onClick = {}
//        ) {
//            Icon(imageVector=Icons.Default.Add,contentDescription = "advance")
//            Text(text="Advance")
//
//        }
//    }
//}
//
//
//}





@Composable
fun PaymentItem( checked: Boolean,onCheckedChange: (Boolean) -> Unit,paymentMonth: PaymentMonth) {
    ListItem( leadingContent = { Text(text=" \u20B9 ${paymentMonth.fee}", fontWeight = FontWeight.Bold)},
        headlineContent = { Text(text=paymentMonth.month,fontWeight= FontWeight.ExtraBold) },
        colors=ListItemDefaults.colors(containerColor = if (checked) MaterialTheme.colorScheme.tertiaryContainer else Color.White,
            leadingIconColor = if (checked) Color(red=0,green=0,blue=0) else Color.Gray),
        trailingContent = { Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange


        )}
    )
}

@Composable
fun PaymentItem2(month:String,fee:Int,checked:Boolean,onCheckedChange: (Boolean) -> Unit) {
    ListItem( leadingContent = { Text(text=" \u20B9 $fee", fontWeight = FontWeight.Bold)},
        headlineContent = { Text(text=month,fontWeight= FontWeight.ExtraBold) },
        colors=ListItemDefaults.colors(containerColor = if (checked) Color.Blue else Color.White,
            leadingIconColor = if (checked) Color(red=0,green=0,blue=0) else Color.Gray),
        trailingContent = { Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange


        )}
    )
}
@Composable
fun PaymentTextItem(text:String) {
    Column(modifier= Modifier.fillMaxWidth())
    {
        Text(modifier=Modifier.align(Alignment.CenterHorizontally),
            text = text, fontSize = 20.sp, color = Color.Red,
            fontWeight = FontWeight.Bold
        )

    }
}

@Preview
@Composable
fun PaymentItemPreview() {
    var checked by remember { mutableStateOf(false) }
    PaymentItem2(fee=500,month="",checked =checked , onCheckedChange ={_ -> checked =!checked }  )

}

@Preview
@Composable
fun PaymentScreenPreview() {

    PaymentScreen(paymentViewModel = remember{PaymentViewModel()}, onNavigateBack = {})
}







