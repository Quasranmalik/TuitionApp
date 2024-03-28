package com.example.myapplication.ui.payment

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.assertj.core.api.Assertions.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.kotlin.verify


@RunWith(RobolectricTestRunner::class)
class PaymentConfirmationScreenKtTest{
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var paymentConfirmationViewModel: PaymentConfirmationViewModel

    val paymentAmountState = MutablePaymentAmountState(name="Name",amount=500)


    fun setContent(increase:Boolean?=null){
        composeTestRule.setContent {
            val paymentAmountState = MutablePaymentAmountState(name="Name",amount=500).apply{this.increase=increase}

            PaymentConfirmation(
                paymentAmountState = paymentAmountState,
                onAdjustmentAmountChanged = {paymentAmountState.adjustmentAmount=it;paymentAmountState.apply{
                    totalAmount = when(increase){
                        true -> amount + (adjustmentAmount.toIntOrNull()?:0)
                        false -> amount - (adjustmentAmount.toIntOrNull()?:0)
                        null ->  amount
                    }
                } },
                onAdd = paymentConfirmationViewModel::onAdd,
                onSubtract = paymentConfirmationViewModel::onSubtract, onRemove = {paymentAmountState.increase = null;paymentAmountState.adjustmentAmount=""},
                onPay = {}, onNavigateBack = {})
        }
    }

    @Test
    fun opening_adjustment_and_clicking_remove_to_check_app_crash(){
        setContent()
        assertThat(paymentAmountState.increase).isNull()
        val plusButton = composeTestRule.onNodeWithContentDescription("increase").assertIsEnabled().assertIsDisplayed()
        val minusButton = composeTestRule.onNodeWithContentDescription("decrease").assertIsEnabled()
//        plusButton.performClick().assertIsEnabled()
        plusButton.performClick()
        verify(paymentConfirmationViewModel).onAdd()
//        minusButton.performClick()
//        assertThat(paymentAmountState.increase).isEqualTo(true)
//        composeTestRule.onNodeWithContentDescription("delete").assertExists()
//            .performClick().assertIsNotDisplayed()

    }

    @Test
    fun clicking_on_plus_button_disables_the_minus_button(){
        setContent()
        assertThat(paymentAmountState.increase).isNull()
        val plusButton = composeTestRule.onNodeWithContentDescription("increase").assertIsEnabled()
        val minusButton = composeTestRule.onNodeWithContentDescription("decrease").assertIsEnabled()
        plusButton.performClick().assertIsEnabled()
        assertThat(paymentAmountState.increase).isEqualTo(true)
        minusButton.assertIsNotEnabled()
    }

    @Test
    fun clicking_on_minus_button_disables_the_plus_button(){
        setContent()
        assertThat(paymentAmountState.increase).isNull()
        composeTestRule.onNodeWithContentDescription("delete").assertDoesNotExist()
        val plusButton = composeTestRule.onNodeWithContentDescription("increase").assertIsEnabled()
        val minusButton = composeTestRule.onNodeWithContentDescription("decrease").assertIsEnabled()
        minusButton.performClick().assertIsEnabled()
        assertThat(paymentAmountState.increase).isEqualTo(false)
        plusButton.assertIsNotEnabled()
    }

    @Test
    fun setting_increase_manually_to_true_and_checking_plus_minus_button_state(){
        setContent(increase = true)
        composeTestRule.onNodeWithContentDescription("increase").assertIsEnabled()
        composeTestRule.onNodeWithContentDescription("decrease").assertIsNotEnabled()
        composeTestRule.onNodeWithContentDescription("delete").assertIsDisplayed()

    }

    @Test
    fun setting_increase_manually_to_false_and_checking_plus_minus_button_state(){
        setContent(increase = false)
        composeTestRule.onNodeWithContentDescription("increase").assertIsNotEnabled()
        composeTestRule.onNodeWithContentDescription("decrease").assertIsEnabled()
        composeTestRule.onNodeWithContentDescription("delete").assertExists()

    }

}