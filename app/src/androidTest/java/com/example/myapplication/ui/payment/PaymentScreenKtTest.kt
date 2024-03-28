package com.example.myapplication.ui.payment
//
//import android.app.role.RoleManager
//import androidx.compose.ui.semantics.Role
//import androidx.compose.ui.semantics.SemanticsProperties
//import androidx.compose.ui.test.SemanticsMatcher
//import androidx.compose.ui.test.assert
//import androidx.compose.ui.test.assertCountEquals
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.assertIsNotDisplayed
//import androidx.compose.ui.test.assertIsOff
//import androidx.compose.ui.test.assertIsOn
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onAllNodesWithText
//import androidx.compose.ui.test.onFirst
//import androidx.compose.ui.test.onLast
//import androidx.compose.ui.test.onNodeWithContentDescription
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.onRoot
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.performScrollToKey
//import androidx.compose.ui.test.printToLog
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.junit.MockitoJUnit
//import org.mockito.kotlin.verify
//import org.assertj.core.api.Assertions.*
//import org.mockito.kotlin.any
//import org.mockito.kotlin.eq
//
//class PaymentScreenKtTest{
//
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @get:Rule
//    val mockitoRule = MockitoJUnit.rule()
//
//    @Mock
//    private lateinit var paymentViewModel: PaymentViewModel
//
//    private lateinit var  paymentUiState :MutablePaymentUiState
//    val checkBoxMatcher = SemanticsMatcher.expectValue(SemanticsProperties.Role,Role.Checkbox)
//
//
//    fun setContent(numberOfMonths :Int=2,selectedTill:Int=-1){
//        paymentUiState=TestPaymentUiState(numberOfMonths=numberOfMonths,selectedTill=selectedTill)
//        composeTestRule.setContent {
//            PaymentScreen(
//                paymentUiState = paymentUiState,
//                onSelectedMonthChange =  {paymentUiState.selectedTill=it},
//                onAdvance = paymentViewModel::advance,
//                onPay = paymentViewModel::pay, onNavigateBack = paymentViewModel::onNavigateBack)
//        }
//    }
//
//    @Test
//    fun buttonExistsButIsNotDisplayed(){
//        setContent()
//        composeTestRule.onAllNodesWithText("Advance").onLast().
//        assert(SemanticsMatcher.expectValue(SemanticsProperties.Role,Role.Button)).assertIsNotDisplayed()
////        verify(paymentViewModel).advance()
//    }
//
//
//
//    @Test
//    fun selectingAllMonthsAndCheckingAdvanceButtonIsDisplayed(){
//        val numberOfMonths=4
//        setContent(numberOfMonths = numberOfMonths)
//        composeTestRule.onNodeWithTag("PaymentList").performScrollToKey(2*numberOfMonths)
//        composeTestRule.onAllNodes(checkBoxMatcher).onLast().performClick()
//        assertThat(paymentUiState.selectedTill).isEqualTo(2*numberOfMonths-1)
////        verify(paymentViewModel).onSelectedMonthChange(eq(2*numberOfMonths-1))
////        composeTestRule.onRoot().printToLog("Tag")
//
//
//
////        assertThat(paymentUiState.selectedTill).isEqualTo(2*numberOfMonths-1)
////        composeTestRule.onNodeWithContentDescription("advance").assertIsDisplayed()
//    }
//
//
//
//}
//
//
//
