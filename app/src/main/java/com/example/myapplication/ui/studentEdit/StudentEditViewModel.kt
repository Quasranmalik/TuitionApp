package com.example.myapplication.ui.studentEdit

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.example.myapplication.data.student.StudentRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EditViewModel  constructor(
    private val savedState: SavedStateHandle,
    private val studentRepository: StudentRepository
){

    private val _studentState = MutableStudentState()

    public val studentState:StudentState=_studentState
    fun onFirstNameChanged (firstName: String){
        _studentState.firstName=firstName
    }
     fun onLastNameChanged(lastName: String){
         _studentState.lastName=lastName
     }

    fun onStandardChanged(standard: String){
        _studentState.standard=standard
    }

    fun onFeeChanged(fee: String){
        _studentState.fee=fee
    }

    fun onDateChanged(date:String){
        _studentState.date=date
    }

    fun toggleEdit(){
        _studentState.edit=!_studentState.edit
    }

    fun loadStudent(){}

    fun save(){}

    fun edit(){}

    fun navigateBack(){}
}


@VisibleForTesting
 private class MutableStudentState(firstName:String ="",lastName: String="",standard:String="",fee:String="",date:String="",override val new:Boolean=false,edit:Boolean=false):StudentState {
    override var firstName : String  by mutableStateOf(firstName)

    override var lastName  : String  by mutableStateOf(lastName)

    override var standard  : String  by mutableStateOf(standard)

    override var fee       : String  by mutableStateOf(fee)

    override var date      : String  by mutableStateOf(date)


    override var edit      : Boolean by mutableStateOf(edit)




}

@Stable
interface StudentState{
    val firstName:String
    val lastName:String
    val standard:String
    val fee:String
    val date:String
    val edit:Boolean
    val new:Boolean

}

data class TestStudentState(override val firstName: String="Abcd",
                            override val lastName: String = "Wxyz",
                            override val standard: String = "0",
                            override val fee: String ="100",
                            override val date: String = "12/12/2022",
                            override val edit: Boolean = true,
                            override val new: Boolean = false):StudentState


 val StudentSaver = listSaver<StudentState,Any?>(
    save = {listOf(    it.firstName,it.lastName,it.standard.toIntOrNull(),it.fee.toIntOrNull(),
        if (it.date != ""){ LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd/MM/yyyy")).toEpochDay() } else null ,it.new, it.edit) },
    restore = {     MutableStudentState(  it[0] as String,it[1] as String,(it[2] as Int?).toString(),(it[3] as Int?).toString(),
        (it[4] as Long?)?.let{ LocalDate.ofEpochDay(it).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} ?:"" ,it[5] as Boolean,it[6] as Boolean)      }
)

