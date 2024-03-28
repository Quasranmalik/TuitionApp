package com.example.myapplication.ui.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun PaymentCard(modifier:Modifier =Modifier,startMonth:String,lastMonth:String,amount:Int) {
    Card (modifier=modifier){
        Column(modifier=Modifier.padding(vertical=15.dp, horizontal = 2.dp),horizontalAlignment = Alignment.CenterHorizontally)  {
          Row {
              Text(text=startMonth, fontSize = 20.sp)
                Text(text=" - ", fontSize = 20.sp)
              Text(text=lastMonth,fontSize = 20.sp)
          }
            Spacer(modifier=Modifier.height(20.dp))
            Text(text= "₹$amount", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        }
    }
}

//@Preview(showSystemUi = true, showBackground = false)
@Composable
fun PaymentCardPreview() {

    Box{
        Card(modifier= Modifier.align(Alignment.TopEnd)) {
            Text(text="Name",modifier=Modifier.padding(10.dp))
        }

        Box(modifier=Modifier.align(Alignment.Center)) {
            Row {
                Column(modifier=Modifier.alignBy(FirstBaseline)){
                    Text(text="5 months",  fontWeight = FontWeight.Light)
                    Spacer(modifier=Modifier.height(15.dp))
                    Text(text="₹500 X 5",fontStyle = FontStyle.Italic,fontWeight = FontWeight.Light)
                }
                Spacer(modifier=Modifier.width(5.dp))
                PaymentCard(modifier=Modifier.alignBy(FirstBaseline),startMonth = "October 2023", lastMonth = "November 2023", amount = 1500 )

            }
        }


    Row(modifier= Modifier
        .align(Alignment.BottomEnd)
        .padding(5.dp)){
        OutlinedButton(onClick = { /*TODO*/ },modifier=Modifier.padding(horizontal=4.dp)) {
            Text(text="Cancel")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text="Pay")
        }
    }


    }



}


@Composable
fun PaymentDialogContent() {
    Row {
        Column(modifier=Modifier.alignBy(FirstBaseline)){
            Text(text="5 months",  fontWeight = FontWeight.Light)
            Spacer(modifier=Modifier.height(15.dp))
            Text(text="₹500 X 5",fontStyle = FontStyle.Italic,fontWeight = FontWeight.Light)
        }
        Spacer(modifier=Modifier.width(5.dp))
        PaymentCard(modifier=Modifier.alignBy(FirstBaseline),startMonth = "October 2023", lastMonth = "November 2023", amount = 1500 )

    }
}

@Composable
fun PaymentConfirmation(paymentAmountState: PaymentAmountState,
                        onAdjustmentAmountChanged :(String) -> Unit,onAdd:() ->Unit,onSubtract:() -> Unit,
                        onRemove:() -> Unit,onPay : () -> Unit,onNavigateBack : () -> Unit) {

    val plusButtonEnabled by remember{ derivedStateOf { paymentAmountState.increase?:true }}
    val minusButtonEnabled by remember{ derivedStateOf { paymentAmountState.increase?.let{!it}?:true }}
    val adjusting by remember{ derivedStateOf { paymentAmountState.increase != null }}
    Box(modifier=Modifier.fillMaxSize()){

        Card(modifier=Modifier.align(Alignment.TopEnd)) {
            Text(modifier=Modifier.padding(horizontal=10.dp,vertical=5.dp),text= paymentAmountState.name,fontSize=25.sp)
        }
        Column(modifier= Modifier
            .align(Alignment.TopCenter)
            .offset(y = 60.dp)
            .fillMaxWidth()
            ){

            val alignmentPosition = with(LocalDensity.current){210.dp.roundToPx()}
            Row(verticalAlignment = Alignment.CenterVertically,modifier=Modifier.alignBy{_ -> alignmentPosition  })
            {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier= Modifier
                        .width(160.dp)
                        .padding(end = 10.dp)){
                    Text(text="Fee", fontWeight = FontWeight.Bold,fontSize=25.sp)
                    Text("₹5000x15", modifier=Modifier.widthIn(0.dp,80.dp),fontStyle = FontStyle.Italic,fontSize=18.sp)
                }


                Spacer(Modifier.width(20.dp))




                Button(onClick =onSubtract, modifier= Modifier
                    .size(25.dp)
                    .align(Alignment.CenterVertically),
                    shape= CircleShape, enabled =minusButtonEnabled,
                    colors= ButtonDefaults.buttonColors(containerColor =Color.Red ),
                    contentPadding = PaddingValues(0.dp)

                ){
                    Icon(painter= painterResource(id = R.drawable.remove_fill0_wght400_grad0_opsz48),contentDescription = "decrease")

                }
                Spacer(Modifier.width(5.dp))
                Surface(modifier=Modifier.width(120.dp),shape= RoundedCornerShape(10.dp),
                    color=MaterialTheme.colorScheme.surfaceVariant) {
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp), contentAlignment = Alignment.Center){
                        Text(buildAnnotatedString {
                            withStyle(SpanStyle(fontSize=25.sp)){
                                append("₹  ")
                            }
                            withStyle(SpanStyle(fontSize=20.sp)){
                                append("${paymentAmountState.amount}")
                            }
                        })

                    }
                }
                Spacer(Modifier.width(5.dp))
                Button(onClick = onAdd, modifier= Modifier
                    .size(25.dp)
                    .align(Alignment.CenterVertically),
                    shape= CircleShape,enabled =plusButtonEnabled,
                    colors= ButtonDefaults.buttonColors(containerColor =Color.Red ), contentPadding = PaddingValues(0.dp)
                ){
                    Icon(Icons.Default.Add,contentDescription = "increase")

                }
            }


            AnimatedVisibility(visible = adjusting,
                modifier=Modifier.alignBy{_ -> alignmentPosition  }) {
                Column {
                    Spacer(Modifier.height(15.dp))

                    Divider()
                    Spacer(Modifier.height(15.dp))

                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier= Modifier
                                .width(210.dp)
                                .padding(end = 10.dp)){
                            Text(text=when(paymentAmountState.increase){
                                                                       true -> "Collect previous"
                                                                        false -> "Discount"
                                                                        else -> ""
                                                                       },
                                fontWeight = FontWeight.Bold,fontSize=25.sp)


                        }


                        TextField(value = paymentAmountState.adjustmentAmount, onValueChange = onAdjustmentAmountChanged,
                            modifier=Modifier.width(120.dp),
                            textStyle = TextStyle(fontSize = 20.sp),
                            leadingIcon={Icon(painter= painterResource(id =
                            R.drawable.currency_rupee_fill0_wght400_grad0_opsz48
                            ),contentDescription = null)},
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                        IconButton(onClick = onRemove) {
                            Icon(imageVector = Icons.Filled.Delete,contentDescription = "delete")
                        }
                    }
                }


            }



        }
        Card (modifier=Modifier.align(Alignment.Center)){
            Column(modifier=Modifier.padding(vertical=15.dp, horizontal = 10.dp),horizontalAlignment = Alignment.CenterHorizontally)  {
                Row {
                    Text(text="October 2023", fontSize = 20.sp)
                    Text(text=" - ", fontSize = 20.sp)
                    Text(text="November 2023",fontSize = 20.sp)
                }
                Spacer(modifier=Modifier.height(20.dp))
                Text(text= "₹${paymentAmountState.totalAmount}", fontWeight = FontWeight.Bold, fontSize = 30.sp)
            }
        }

        Row(modifier=Modifier.align(Alignment.BottomEnd)){
            OutlinedButton(onClick = onNavigateBack) {
                Text(text = "Cancel", fontSize = 25.sp)
            }
            Spacer(Modifier.width(20.dp))
            Button(onClick = onPay,modifier=Modifier.width(125.dp)) {
                Text("Pay",fontSize = 25.sp)
            }
        }


    }
}

@Preview
@Composable
fun PaymentConfirmationPreview() {
    val paymentAmountState = MutablePaymentAmountState(name="Name",amount=500)
    PaymentConfirmation(
        paymentAmountState = paymentAmountState,
        onAdjustmentAmountChanged = {paymentAmountState.adjustmentAmount=it;paymentAmountState.apply{
            totalAmount = when(increase){
                true -> amount + (adjustmentAmount.toIntOrNull()?:0)
                false -> amount - (adjustmentAmount.toIntOrNull()?:0)
                null ->  amount
            }
        } },
        onAdd = { paymentAmountState.increase =true },
        onSubtract = { paymentAmountState.increase =false }, onRemove = {paymentAmountState.increase = null;paymentAmountState.adjustmentAmount="";paymentAmountState.totalAmount=paymentAmountState.amount},
            onPay={},onNavigateBack={})
}



//@Preview
@Composable
fun AddButtonPreview() {
    Row{
        Button(onClick = {}, modifier= Modifier
            .size(30.dp)
            .align(Alignment.CenterVertically),shape= CircleShape,
            colors= ButtonDefaults.buttonColors(containerColor =Color.Red ), contentPadding = PaddingValues(0.dp)
        ){
            Icon(painter= painterResource(id = R.drawable.remove_fill0_wght400_grad0_opsz48),contentDescription = null)

        }
        Spacer(Modifier.width(5.dp))

        TextField(value = "2345", onValueChange = {},modifier=Modifier.width(120.dp),
            leadingIcon = {Icon(painter= painterResource(id =
                R.drawable.currency_rupee_fill0_wght400_grad0_opsz48
            ),contentDescription = null)}, textStyle = TextStyle(fontSize = 20.sp), readOnly = true)
        Spacer(Modifier.width(5.dp))
        Button(onClick = {}, modifier= Modifier
            .size(30.dp)
            .align(Alignment.CenterVertically),shape= CircleShape,
            colors= ButtonDefaults.buttonColors(containerColor =Color.Red ), contentPadding = PaddingValues(0.dp)
        ){
            Icon(Icons.Default.Add,contentDescription = null)

        }
    }
}

//@Preview
@Composable
fun PaymentAndCancelButtonPreview() {
    Row{
        OutlinedButton(onClick = { /*TODO*/ }) {
            Text(text = "Cancel")
        }
        Spacer(Modifier.width(10.dp))
        Button(onClick = { /*TODO*/ },modifier=Modifier.width(90.dp)) {
            Text("Pay")
        }
    }

}




