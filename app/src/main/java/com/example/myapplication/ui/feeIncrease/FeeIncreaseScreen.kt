package com.example.myapplication.ui.feeIncrease

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeeIncreaseScreen(feeIncreaseScreenState: FeeIncreaseScreenState,
                      onFeeChanged:(String) -> Unit, toggleGoBack:()->Unit, onSave:() -> Unit, onNavigateBack : () -> Unit) {
    val currentToggleGoBack by rememberUpdatedState(newValue = toggleGoBack)
    val currentOnNavigateBack by rememberUpdatedState(newValue = onNavigateBack)

    val lifecycle= LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle){
        val observer=LifecycleEventObserver{_,event ->

            if (event == Lifecycle.Event.ON_STOP){
                currentToggleGoBack()
            }

        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(feeIncreaseScreenState.goBack){
        if (feeIncreaseScreenState.goBack) currentOnNavigateBack()
    }





    Column{

        CenterAlignedTopAppBar(title = { Text("Change Fee") },
            navigationIcon = { IconButton(onClick = onNavigateBack) {
                Icon(imageVector= Icons.AutoMirrored.Sharp.ArrowBack,contentDescription = null)
            }},
            actions={ IconButton(onClick = { onSave();onNavigateBack() }) {
                Icon(imageVector =Icons.Sharp.Check , contentDescription =null )
            }})

        FeeField(changedFeeProvider = { feeIncreaseScreenState.changedFee},
            currentFee =feeIncreaseScreenState.currentFee , onFeeChanged =onFeeChanged,
            onSave = onSave )
        Spacer(Modifier.height(70.dp))
        FeeIncreaseCard(modifier= Modifier.align(Alignment.CenterHorizontally),increaseMonth=feeIncreaseScreenState.increaseMonth)


    }

}

@Composable
fun FeeField(modifier:Modifier = Modifier,changedFeeProvider:()->String,currentFee: Int,increase:Boolean=true,
                 onFeeChanged: (String) -> Unit,onSave:() -> Unit) {

    val textField=  remember{FocusRequester()}
    val keyboardController =LocalSoftwareKeyboardController.current


    LaunchedEffect(Unit){
        textField.requestFocus()
        keyboardController?.show()
    }
    val isError by remember{ derivedStateOf {
        val changedFeeInt = changedFeeProvider().toIntOrNull() ?: 0
        if (increase)changedFeeInt <= currentFee else changedFeeInt >= currentFee
    }}




    Box {

        OutlinedTextField(modifier= modifier
            .fillMaxWidth()
            .align(Alignment.Center)
            .focusRequester(textField),value = changedFeeProvider(), onValueChange = onFeeChanged,
            label={Text("Fee")},
            placeholder={ Text(text = "placeholder",
                style= TextStyle(color= Color.Black.copy(alpha=0.5f))
            )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {if (!isError){keyboardController?.hide()} }),
            isError=isError

        )
        IconButton(modifier= Modifier.align(Alignment.CenterEnd),onClick = onSave,
            enabled=!isError,colors=IconButtonDefaults.iconButtonColors(contentColor = Color.Blue)) {
            Icon(modifier= Modifier.size(40.dp),imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "enter")
        }
    }

}





@Composable
fun FeeIncreaseCard(modifier: Modifier=Modifier,increaseMonth:String) {
    Card(modifier= modifier) {
        Column (modifier= Modifier.padding(horizontal=10.dp,vertical =10.dp),horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Fee Increase from Month",color= Color.Blue, fontSize = 25.sp)
            Spacer(Modifier.height(15.dp))
            Text(text =increaseMonth , fontWeight = FontWeight.Bold,fontSize = 25.sp)
        }
    }

}


@Preview
@Composable
fun FeeIncreaseCardPreview() {
    FeeIncreaseCard(increaseMonth = "3 Aug 2023 - 3 Sept 2023")
}


@Preview(showSystemUi = true)
@Composable
fun FeeFieldPreview() {

    FeeField(changedFeeProvider = {"500"}, onFeeChanged = {}, currentFee = 100, onSave = {})
}

