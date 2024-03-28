package com.example.myapplication.ui.feeIncrease

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FeeIncreaseScreenViewModel (savedState:SavedStateHandle){

    private val _feeIncreaseScreenState = MutableFeeIncreaseScreenState(currentFee = checkNotNull(savedState["currentFee"]),
        increaseMonth = getMonth(checkNotNull(savedState["increaseMonth"]) ),
        increase = checkNotNull(savedState["increase"])
    )

    public val feeIncreaseScreenState : FeeIncreaseScreenState = _feeIncreaseScreenState


    private fun getMonth(epochDay:Long) =
        with( LocalDate.ofEpochDay(epochDay)){
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
            this.format(formatter)+ " "+
                    this.plusMonths(1).format(formatter)
       }

}

private class MutableFeeIncreaseScreenState(override val currentFee: Int, override val increaseMonth: String,override val increase:Boolean):
    FeeIncreaseScreenState {

    override var changedFee by mutableStateOf("")
    override var goBack by mutableStateOf(false)


}

interface FeeIncreaseScreenState{
    val currentFee:Int
    val changedFee:String
    val goBack:Boolean
    val increaseMonth:String
    val increase:Boolean
}