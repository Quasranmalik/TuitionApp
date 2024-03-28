//package com.example.myapplication.ui.edit
//
//import androidx.compose.ui.test.assertHasClickAction
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.isDialog
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithContentDescription
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.junit.MockitoJUnit
//import org.mockito.kotlin.inOrder
//import org.mockito.kotlin.verify
//import org.mockito.kotlin.verifyNoMoreInteractions
//import org.robolectric.RobolectricTestRunner
//import org.assertj.core.api.Assertions.*
//
//@RunWith(RobolectricTestRunner::class)
//class AddStudentScreenKtTest{
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    private lateinit var studentState: MutableStudentState
//
//    @get:Rule
//    val mockitoRule = MockitoJUnit.rule()
//
//    @Mock
//    private lateinit var editViewModel: EditViewModel
//
//
//
//
//
//    @Test
//    fun viewAndExit(){
//        setState(new = false,edit=false)
//        composeTestRule.onNodeWithContentDescription("back").performClick()
//        verify(editViewModel).navigateBack()
//        verifyNoMoreInteractions(editViewModel)
//    }
//
//    @Test
//    fun studentEditAndDiscardTest(){
//        setState(new=false,edit=false)
//        with(composeTestRule){
//            onNodeWithContentDescription("edit").performClick()
//          onNodeWithContentDescription("back").performClick()
////            Check the confirmation dialog appears
//            onNode(isDialog()).assertIsDisplayed()
//           onNodeWithText("Discard").performClick().assertDoesNotExist()
//
//            onNodeWithContentDescription("back").performClick()
//            inOrder(editViewModel){
//                verify(editViewModel).loadStudent()
//                verify(editViewModel).navigateBack()
//                verifyNoMoreInteractions(editViewModel)
//            }
//
//        }
//
//    }
//    @Test
//    fun studentEditAndCancel(){
//
//        setState(new=false,edit=false)
//        with(composeTestRule){
//            onNodeWithContentDescription("edit").performClick()
//
////            Checking title of the AppBar changed to edit
//            onNodeWithText("Edit").assertIsDisplayed()
////            Exiting edit without saving
//            onNodeWithContentDescription("back").performClick()
//
////            Checking if confirmation prompt is shown and clicking cancel
//            onNodeWithText("Cancel").assertExists().performClick().assertDoesNotExist()
//
//            assertThat(studentState.edit).isEqualTo(true)
//
//            verifyNoMoreInteractions(editViewModel)
//
//        }
//    }
//
//    @Test
//    fun studentEditAndSaveTest() {
//        setState(new=false,edit=false)
//        composeTestRule.onNodeWithContentDescription("edit").assertExists().assertHasClickAction().performClick()
//        composeTestRule.onNodeWithText("Edit").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Save").performClick()
//
//        inOrder(editViewModel){
//            verify(editViewModel).navigateBack()
//            verify(editViewModel).save()
//           verifyNoMoreInteractions(editViewModel)
//        }
//
//    }
//
//
//    @Test
//    fun newStudentSaveTest(){
//        setState(new=true,edit=true)
//        composeTestRule.onNodeWithText("New Student").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Save").performClick()
//        inOrder(editViewModel){
//            verify(editViewModel).navigateBack()
//            verify(editViewModel).save()
//            verifyNoMoreInteractions(editViewModel)
//
//        }
//    }
//
//    @Test
//    fun newStudentDiscardTest(){
//        setState(new=true,edit=true)
//        composeTestRule.onNodeWithText("New Student").assertIsDisplayed()
//        composeTestRule.onNodeWithContentDescription("back").performClick()
//        composeTestRule.onNodeWithText("Discard").assertIsDisplayed().performClick()
//        inOrder(editViewModel){
//            verify(editViewModel).navigateBack()
//            verifyNoMoreInteractions(editViewModel)
//        }
//    }
//
//
//    fun setState(new:Boolean,edit:Boolean){
//        composeTestRule.setContent {
//             studentState  =  MutableStudentState(new=new,edit=edit)
//            StudentScreen(
//                studentState = studentState ,
//                onFirstNameChanged = {},
//                onLastNameChanged ={} ,
//                onStandardChanged ={} ,
//                onFeeChanged = {},
//                onDateChanged = {},
//                toggleEdit = { studentState.edit=!studentState.edit },
//                loadStudent = editViewModel::loadStudent,
//                save = editViewModel::save, onNavigateBack = editViewModel::navigateBack)
//        }
//    }
//}
//
