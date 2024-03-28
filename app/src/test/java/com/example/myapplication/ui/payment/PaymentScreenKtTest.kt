package com.example.myapplication.ui.payment
//
//import androidx.compose.ui.semantics.Role
//import androidx.compose.ui.semantics.SemanticsProperties
//import androidx.compose.ui.test.SemanticsMatcher
//import androidx.compose.ui.test.assert
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.assertIsNotDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onAllNodesWithText
//import androidx.compose.ui.test.onLast
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.performScrollToIndex
//import org.assertj.core.api.Assertions.*
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.junit.MockitoJUnit
//import org.mockito.kotlin.eq
//import org.mockito.kotlin.verify
//import org.robolectric.RobolectricTestRunner
//
//@RunWith(RobolectricTestRunner::class)
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
//    private lateinit var  paymentUiState :TestPaymentUiState
//    val checkBoxMatcher = SemanticsMatcher.expectValue(SemanticsProperties.Role,Role.Checkbox)
//
//
//    fun setContent(numberOfMonths :Int=2,selectedTill:Int=-1){
//        paymentUiState=TestPaymentUiState(numberOfMonths=numberOfMonths,selectedTill=selectedTill)
//        composeTestRule.setContent {
//            PaymentScreen(
//                paymentUiState = paymentUiState,
//                onSelectedMonthChange = {paymentViewModel.onSelectedMonthChange(it); paymentUiState.selectedTill=it},
//                onAdvance = paymentViewModel::advance,
//                onPay = paymentViewModel::pay, onNavigateBack = paymentViewModel::onNavigateBack)
//        }
//    }
//
//    @Test
//    fun buttonExistsButIsNotDisplayed(){
//        setContent()
//      composeTestRule.onAllNodesWithText("Advance").onLast().
//      assert(SemanticsMatcher.expectValue(SemanticsProperties.Role,Role.Button)).assertIsNotDisplayed()
////        verify(paymentViewModel).advance()
//    }
//
//
//
//    @Test
//    fun selectingAllMonthsAndCheckingAdvanceButtonIsDisplayed(){
//        val numberOfMonths=4
//        setContent(numberOfMonths = numberOfMonths)
//        composeTestRule.onNodeWithTag("PaymentList").performScrollToIndex(2*numberOfMonths)
//        composeTestRule.onNodeWithText("Cancel").assertExists().assertIsDisplayed()
//        val lastCheckBox = composeTestRule.onAllNodes(checkBoxMatcher).onLast().assertIsDisplayed().performClick()
////        lastCheckBox.assertIsNotDisplayed()
//        verify(paymentViewModel).onSelectedMonthChange(eq(2*numberOfMonths-1))
////        assertThat(paymentUiState.selectedTill).isEqualTo(2*numberOfMonths-1)
//
//
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
