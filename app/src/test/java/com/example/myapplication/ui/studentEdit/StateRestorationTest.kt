//package com.example.myapplication.ui.edit
//
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.test.assertIsEnabled
//import androidx.compose.ui.test.assertIsNotEnabled
//import androidx.compose.ui.test.assertTextEquals
//import androidx.compose.ui.test.junit4.StateRestorationTester
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.onParent
//import androidx.compose.ui.test.performTextInput
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.shadows.ShadowLog
//
//@RunWith(RobolectricTestRunner::class)
//class StateRestorationTest {
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    private val restorationTester= StateRestorationTester(composeTestRule)
//
//    @Before
//    fun setUp(){
//        ShadowLog.stream=System.out
//    }
//
//    @Test
//    fun onRecreation_stateIsRestored(){
//
//        restorationTester.setContent {
//            val studentState = rememberSaveable(saver = StudentSaver) {
//                MutableStudentState().apply{edit=true}
//            }
//
//           InputField(
//               studentState = studentState,
//               onFirstNameChanged = {studentState.firstName=it},
//               onLastNameChanged = {studentState.lastName=it},
//               onStandardChanged ={studentState.standard=it} ,
//               onFeeChanged = {studentState.fee=it},
//               onDateChanged = {studentState.date=it}
//           )
//        }
//
//        with(composeTestRule){
//            onNodeWithText("First Name", useUnmergedTree = true).onParent().onParent().performTextInput("Abcd")
//            onNodeWithText("Last Name", useUnmergedTree = true).onParent().onParent().performTextInput("Pqrs")
//            onNodeWithText("Class", useUnmergedTree = true).onParent().onParent().performTextInput("3")
//            onNodeWithText("Fee", useUnmergedTree = true).onParent().onParent().performTextInput("100")
//
//
//        }
//
//        restorationTester.emulateSavedInstanceStateRestore()
//
//        with(composeTestRule){
//            onNodeWithText("First Name", useUnmergedTree = true).onParent().onParent().assertTextEquals("Abcd")
//            onNodeWithText("Last Name", useUnmergedTree = true).onParent().onParent().assertTextEquals("Pqrs")
//            onNodeWithText("Class", useUnmergedTree = true).onParent().onParent().assertTextEquals("3")
//            onNodeWithText("Fee", useUnmergedTree = true).onParent().onParent().assertTextEquals("100")
//
//
//        }
//
//
//    }
//
//    @Test
//    fun dateRestore(){
//        restorationTester.setContent {
//            val studentState = rememberSaveable(saver = StudentSaver) {
//                MutableStudentState().apply{edit=false}
//            }
//
//
//            OutlinedTextField(value = studentState.date, onValueChange ={studentState.date=it} ,modifier= Modifier.testTag("test"),enabled = studentState.edit )
//
//        }
//
//        restorationTester.emulateSavedInstanceStateRestore()
//        composeTestRule.onNodeWithTag("test").assertTextEquals("").assertIsNotEnabled()
//
//    }
//
//
//}