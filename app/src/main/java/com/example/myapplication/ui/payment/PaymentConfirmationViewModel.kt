package com.example.myapplication.ui.payment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class PaymentConfirmationViewModel {
    private val _paymentAmountState = MutablePaymentAmountState(name="name",amount=500)

    val paymentAmountState =_paymentAmountState


    fun onAdd(){
        _paymentAmountState.increase=true
    }

    fun onSubtract(){
        _paymentAmountState.increase=false
    }

    fun onRemove(){
        _paymentAmountState.increase=null
    }

    fun onAdjustmentAmountChanged(adjustmentAmount:String){
        val adjustmentInt = adjustmentAmount.toIntOrNull() ?: 0

        if (!_paymentAmountState.increase!! &&
            adjustmentInt> _paymentAmountState.amount){return}
        _paymentAmountState.adjustmentAmount=adjustmentAmount
        _paymentAmountState.apply{
            totalAmount = when(increase){
                true -> amount + adjustmentInt
                false -> amount - adjustmentInt
                null ->  amount
            }
        }
    }



}


interface PaymentAmountState{
    val name:String
    val amount:Int
    val totalAmount:Int
    val adjustmentAmount:String
    val increase:Boolean?
}

class MutablePaymentAmountState(override val name : String,override val amount:Int):PaymentAmountState{
    override var totalAmount by mutableIntStateOf(amount)
    override var adjustmentAmount by mutableStateOf("")

    override var increase by mutableStateOf<Boolean?>(null)
}