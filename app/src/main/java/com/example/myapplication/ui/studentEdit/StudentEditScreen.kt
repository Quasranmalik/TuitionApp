 package com.example.myapplication.ui.studentEdit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Preview
@Composable
fun TextFieldDateSelectPreview2() {
    var date by rememberSaveable {
        mutableStateOf("")
    }

    TextFieldDateSelect(date = date, onDateChanged ={date=it},readOnly = false)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldDateSelect(date:String,onDateChanged: (String) -> Unit,readOnly:Boolean) {
    val openDialog = remember { mutableStateOf(false) }
    val dateScope = rememberCoroutineScope()




    if (openDialog.value) {
        val datePickerState = rememberDatePickerState(initialDisplayedMonthMillis =
        with(LocalDate.now()){
            this.minusDays(dayOfMonth.toLong()-1).atStartOfDay(ZoneId.of("UTC")).toEpochSecond() *1000

        }
        )

        val confirmEnabled = remember {derivedStateOf { datePickerState.selectedDateMillis != null }}
        DatePickerDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        datePickerState.selectedDateMillis
                        dateScope.launch {
                            datePickerState.selectedDateMillis?.let{
                                val selectedDate =Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC"))
                                    .toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                onDateChanged(selectedDate)
                            }
                        }

                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
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
        ) {
            DatePicker(state = datePickerState)
        }
    }





    OutlinedTextField(value = date, onValueChange = {}, readOnly = readOnly,
        interactionSource =  remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release && !readOnly) {
                            openDialog.value = true
                        }
                    }
                }
            } ,

        label = { Text("Date of joining")},
        placeholder = { Text( "dd/mm/yyyy") },
        trailingIcon = {Icon(painter = painterResource(id =
            R.drawable.calendar_month_fill0_wght400_grad0_opsz48
        ),contentDescription = null)})

    
}





@Composable
fun InputField(studentState:StudentState,onFirstNameChanged:(String)->Unit,
               onLastNameChanged: (String)->Unit,onStandardChanged : (String)->Unit,onFeeChanged: (String)->Unit,onDateChanged : (String)->Unit) {

    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(20.dp)){

        Column(modifier= Modifier.weight(1.0f) , verticalArrangement = Arrangement.spacedBy(50.dp)){
            OutlinedTextField( value = studentState.firstName , onValueChange ={if (studentState.edit) onFirstNameChanged(it)} ,
                label = {Text("First Name")}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), readOnly = !studentState.edit)
            OutlinedTextField( value =studentState.standard  , onValueChange = {if (studentState.edit) onStandardChanged(it)} ,
                label = {Text("Class")}  ,  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), readOnly = !studentState.edit)
            TextFieldDateSelect(date = studentState.date , onDateChanged =onDateChanged, readOnly = !studentState.edit)

        }

        Column(modifier= Modifier.weight(1.0f),verticalArrangement = Arrangement.spacedBy(50.dp)){
            OutlinedTextField(value = studentState.lastName  , onValueChange =  {if (studentState.edit) onLastNameChanged(it)} ,
                label = {Text("Last Name")}   , keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),readOnly=!studentState.edit)
            OutlinedTextField(value = studentState.fee , onValueChange = {if (studentState.edit) onFeeChanged(it)} ,
                label = {Text("Fee")}, leadingIcon = { Icon(painter = painterResource(id =
                R.drawable.currency_rupee_fill0_wght400_grad0_opsz48
                ), contentDescription = null)},keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),readOnly=!studentState.edit)
        }
    }


}


@Composable
fun InputField(new:Boolean,readOnly: Boolean,firstName:String,lastName:String,standard:String,fee:String,date:String,
               onFirstNameChanged: (String) ->Unit,onLastNameChanged : (String) -> Unit,onStandardChanged: (String)->Unit,
               onFeeChanged : (String) -> Unit,onDateChanged:(String) -> Unit) {

    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(20.dp)){

        Column(modifier= Modifier.weight(1.0f) , verticalArrangement = Arrangement.spacedBy(50.dp)){
            OutlinedTextField( value = firstName , onValueChange =if (readOnly){{}} else onFirstNameChanged ,
                label = {Text("First Name")}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), readOnly = readOnly)
            OutlinedTextField( value =standard  , onValueChange = if (readOnly){{}} else onStandardChanged ,
                label = {Text("Class")}  ,  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), readOnly = readOnly)
            TextFieldDateSelect(date = date , onDateChanged =onDateChanged, readOnly = !new)

        }

        Column(modifier= Modifier.weight(1.0f),verticalArrangement = Arrangement.spacedBy(50.dp)){
            OutlinedTextField(value = lastName  , onValueChange =  if (readOnly){{}} else onLastNameChanged ,
                label = {Text("Last Name")}   , keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),readOnly=readOnly)
            OutlinedTextField(value = fee , onValueChange = if (readOnly){{}} else onFeeChanged ,
                label = {Text("Fee")}, leadingIcon = { Icon(painter = painterResource(id =
                    R.drawable.currency_rupee_fill0_wght400_grad0_opsz48
                ), contentDescription = null)},keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),readOnly=readOnly)
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScreen(studentState: StudentState,  onFirstNameChanged: (String) -> Unit,
                  onLastNameChanged: (String) -> Unit, onStandardChanged: (String)->Unit,
                  onFeeChanged : (String) -> Unit, onDateChanged:(String) -> Unit,
                  toggleEdit : () -> Unit ,loadStudent: () -> Unit, save : () -> Unit,onNavigateBack:()->Unit) {

    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = "Discard Changes")
            },
            text = {
                Text(text = "Changes made will be lost")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false

                    }
                ) {
                    Text("Cancel")
                }
            },
            dismissButton = {
                TextButton(
                    onClick =
                        when(studentState.new){
                            true -> {{openDialog.value = false; onNavigateBack()}}
                            false ->{{openDialog.value = false;toggleEdit();loadStudent()}}
                        }



                ) {
                    Text("Discard")
                }
            }
        )
    }




    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

Column {
    CenterAlignedTopAppBar(title = {Text(text = if(studentState.new) "New Student" else if (studentState.edit) "Edit" else "")},
        navigationIcon = {
            IconButton(onClick =  if(studentState.edit) {{openDialog.value =true}} else onNavigateBack ) {
                Icon(imageVector = Icons.Filled.ArrowBack,contentDescription = "back")
            }
        },
        actions= if (studentState.edit) {{TextButton(onClick = {onNavigateBack();save()}  ){Text(text="Save")}}}
        else {{
            IconButton(onClick = toggleEdit) {
                Icon(imageVector = Icons.Filled.Edit,contentDescription = "edit")
            }
        }}
        , scrollBehavior = scrollBehavior)
    Spacer(modifier = Modifier.height(10.dp))
    InputField(
        studentState = studentState,
        onFirstNameChanged = onFirstNameChanged,
        onLastNameChanged = onLastNameChanged,
        onStandardChanged = onStandardChanged,
        onFeeChanged = onFeeChanged,
        onDateChanged =onDateChanged
    )
}






}


@Composable
fun StudentScreen(editViewModel: EditViewModel) {
    StudentScreen(
        studentState = editViewModel.studentState,
        onFirstNameChanged = editViewModel::onFirstNameChanged,
        onLastNameChanged = editViewModel::onLastNameChanged,
        onStandardChanged = editViewModel::onStandardChanged,
        onFeeChanged = editViewModel::onFeeChanged,
        onDateChanged = editViewModel::onDateChanged,
        toggleEdit = editViewModel::toggleEdit,
        loadStudent = editViewModel::loadStudent,
        save = editViewModel::save, onNavigateBack = editViewModel::navigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun StudentScreenPreview() {
    val openDialog = remember { mutableStateOf(false) }
    var new by remember { mutableStateOf(false) }
    var edit by remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = "Discard Changes")
            },
            text = {
                Text(text = "Changes made will be lost")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false

                    }
                ) {
                    Text("Cancel")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        edit=!edit
                    }
                ) {
                    Text("Discard")
                }
            }
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    CenterAlignedTopAppBar(title = {Text(text = if(new) "New Student" else if (edit) "Edit" else "")},
        navigationIcon = {
            IconButton(onClick =  if(edit) {{openDialog.value =true}} else {{}} ) {
                Icon(imageVector = Icons.Filled.ArrowBack,contentDescription = "back")
            }
        },
        actions= if (edit) {{TextButton(onClick = {}  ){Text(text="Save")}}}
        else {{
            IconButton(onClick = {edit=!edit}) {
                Icon(imageVector = Icons.Filled.Edit,contentDescription = "edit")
            }
        }}
        , scrollBehavior = scrollBehavior)
}


@Preview(showBackground = true)
@Composable
fun StudentScreenPreview2() {


    InputField(

        studentState = remember{TestStudentState()},
        onFirstNameChanged = {},
        onLastNameChanged = {},
        onStandardChanged ={} ,
        onFeeChanged = {},
        onDateChanged = {}
    )

}