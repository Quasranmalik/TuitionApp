package com.example.myapplication.ui.FeeList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.ui.payment.PaymentTextItem
import java.util.Locale

@Composable
fun MonthFeeScreen() {
    
    
    
}


@Composable
fun MonthList(dueMonths:List<PaymentMonth>, advanceMonths:List<PaymentMonth> = emptyList(), edit:Boolean,
              increasedFee:String, onIncreasedFeeChange : (String) -> Unit,
              feeIncreaseAtProvider:() -> Int, feeIncreaseAtChanged :(Int) -> Unit,
              lazyListState: LazyListState ) {






    LazyColumn(state=lazyListState){
       itemsIndexed(items=dueMonths){index,paymentMonth ->
           val monthState by remember{ derivedStateOf { if (index <  feeIncreaseAtProvider()) MonthState.DECREASE
           else if (index == feeIncreaseAtProvider()) MonthState.INCREASE_ON
           else MonthState.INCREASE
           }}
           MonthItem(monthState = monthState,  month = paymentMonth.month.getDisplayName(java.time.format.TextStyle.FULL,
               Locale.US), fee =paymentMonth.fee , edit =edit , onEdit = {})
       }

       itemsIndexed(items=advanceMonths){index,paymentMonth ->

           val monthState by remember{ derivedStateOf { if (index + dueMonths.size <  feeIncreaseAtProvider()) MonthState.DECREASE
           else if (index + dueMonths.size== feeIncreaseAtProvider()) MonthState.INCREASE_ON
           else MonthState.INCREASE
           }}
           if(index==0){
               PaymentTextItem(text = "Advance")
           }

           MonthItem(monthState = monthState, month = paymentMonth.month.getDisplayName(java.time.format.TextStyle.FULL,
               Locale.US), fee =paymentMonth.fee , edit =edit , onEdit = {})


       }
   }

}

@Preview
@Composable
fun MonthListPreview() {

    val mockMonthList = remember{List(5){ PaymentMonth() } }

    MonthList(
        dueMonths = mockMonthList,
        advanceMonths = mockMonthList,
        edit =true ,
        feeIncreaseAtProvider = { 6 },
        lazyListState = rememberLazyListState() ,
        feeIncreaseAtChanged = {},
        increasedFee = "",
        onIncreasedFeeChange = {}
    )
}

@Composable
fun MonthItem(monthState: MonthState, month:String, fee:Int, edit :Boolean, onEdit:() -> Unit) {
    ListItem( leadingContent = { Text(text=" \u20B9 $fee", fontWeight = FontWeight.Bold) },
        headlineContent = { Text(text=month,fontWeight= FontWeight.ExtraBold) },
        colors= ListItemDefaults.colors(containerColor = monthState.containerColor,
            leadingIconColor = Color(red=0,green=0,blue=0)),


        trailingContent = { Surface(
            shape= CircleShape,color= MaterialTheme.colorScheme.surfaceVariant) {
            IconButton(onClick = onEdit,enabled=edit,colors=IconButtonDefaults.iconButtonColors(contentColor = monthState.trailingIconColor)) {
                Icon(imageVector = monthState.trailingIcon, contentDescription = "edit")

            }


        }}

    )
}





//@Preview
@Composable
fun MonthItemPreview() {
    var edit by remember{mutableStateOf(true)}
    MonthItem(monthState = MonthState.INCREASE_ON, month = "", fee =500 , edit =edit,onEdit={edit=!edit} )
    
}



enum class MonthState(val containerColor: Color,val trailingIcon: ImageVector,val trailingIconColor:Color){
    DECREASE(Color.White,Icons.Filled.Edit,Color.Black),
    INCREASE(Color.Blue,Icons.Filled.Edit,Color.Black),
    INCREASE_ON(Color.Blue,Icons.Filled.Delete,Color.Red)


}


//@Preview(showSystemUi = false)
@Composable
fun TextBoxPreview() {
    var text by remember{ mutableStateOf("") }
    Box {
        OutlinedTextField(value = text, onValueChange = {text=it},
            placeholder={ Text(text = "placeholder",style= TextStyle(color=Color.Black.copy(alpha=0.5f)))})
        IconButton(modifier= Modifier.align(Alignment.CenterEnd),onClick = { /*TODO*/ }) {
            Icon(modifier=Modifier.size(40.dp),imageVector = Icons.Filled.ArrowForward, contentDescription = "enter",tint=Color.Blue)
        }
    }

    
}



@Preview(showSystemUi = true)
@Composable
fun DialogPreview() {
    Dialog(onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = false, usePlatformDefaultWidth = false)){
        Surface(modifier=Modifier.fillMaxSize(),
            shape= RoundedCornerShape(0.dp)
        ){
            var text by remember{ mutableStateOf("") }
            Column{
                Box(modifier=Modifier.fillMaxWidth()) {
                    OutlinedTextField(modifier=Modifier.fillMaxWidth(),value = text, onValueChange = {text=it},
                        placeholder={ Text(text = "placeholder",
                            style= TextStyle(color=Color.Black.copy(alpha=0.5f)))},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    IconButton(modifier= Modifier.align(Alignment.CenterEnd),onClick = { /*TODO*/ }) {
                        Icon(modifier=Modifier.size(40.dp),imageVector = Icons.Filled.ArrowForward, contentDescription = "enter",tint=Color.Blue)
                    }
                }
                Spacer(Modifier.height(70.dp))
                Card(modifier=Modifier.align(Alignment.CenterHorizontally)) {
                    Column (modifier=Modifier.padding(horizontal=10.dp,vertical =10.dp),horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "Fee Increased from Month",color=Color.Blue, fontSize = 28.sp)
            Spacer(Modifier.height(15.dp))
                        Text(text = "3 Aug 2023 - 3 Sept 2023", fontWeight = FontWeight.Bold,fontSize = 28.sp)
                    }
                }


            }


        }
    }
}