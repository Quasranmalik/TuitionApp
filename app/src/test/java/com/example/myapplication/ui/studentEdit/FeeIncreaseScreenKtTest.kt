package com.example.myapplication.ui.studentEdit

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class FeeIncreaseScreenKtTest{

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setContent(){
//        composeTestRule.setContent { FeeIncreaseScreen() }
    }
    @Test
    fun textField_focus_is_returned_on_reopening(){
        with(composeTestRule.onNodeWithText("Fee", useUnmergedTree = true).onParent().onParent()){
            assertIsFocused()
            performTextInput("5000")
            performImeAction()
            assertIsNotFocused()
        }


    }

}

