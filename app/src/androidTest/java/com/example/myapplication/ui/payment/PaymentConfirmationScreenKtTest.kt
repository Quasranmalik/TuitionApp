package com.example.myapplication.ui.payment

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
import org.assertj.core.api.Assertions.*


class PaymentConfirmationScreenKtTest{
    @get:Rule
    val composeTestRule = createComposeRule()

    val paymentAmountState = MutablePaymentAmountState(name="Name",amount=500)

    @Before
    fun setContent(){
        composeTestRule.setContent {
            val paymentAmountState = MutablePaymentAmountState(name="Name",amount=500)

            PaymentConfirmation(
                paymentAmountState = paymentAmountState,
                onAdjustmentAmountChanged = {paymentAmountState.adjustmentAmount=it;paymentAmountState.apply{
                    totalAmount = when(increase){
                        true -> amount + (adjustmentAmount.toIntOrNull()?:0)
                        false -> totalAmount - amount
                        null ->  amount
                    }
                } },
                onAdd = { paymentAmountState.increase =true },
                onSubtract = { paymentAmountState.increase =false }, onRemove = {paymentAmountState.increase = null;paymentAmountState.adjustmentAmount=""}
                ,onPay={}, onNavigateBack = {})
        }
    }

    @Test
    fun opening_adjustment_and_clicking_remove_to_check_app_crash(){
        assertThat(paymentAmountState.increase).isNull()
        val plusButton = composeTestRule.onNodeWithContentDescription("increase").assertIsEnabled()
        val minusButton = composeTestRule.onNodeWithContentDescription("decrease").assertIsEnabled()
        plusButton.performClick().assertIsEnabled()

        assertThat(paymentAmountState.increase).isEqualTo(true)
        composeTestRule.onNodeWithContentDescription("delete").assertExists()
            .performClick().assertIsNotDisplayed()

    }

    @Test
    fun clicking_on_plus_button_disables_the_minus_button(){
        assertThat(paymentAmountState.increase).isNull()
        val plusButton = composeTestRule.onNodeWithContentDescription("increase").assertIsEnabled()
        val minusButton = composeTestRule.onNodeWithContentDescription("decrease").assertIsEnabled()
        plusButton.performClick().assertIsEnabled()
        assertThat(paymentAmountState.increase).isEqualTo(true)
        minusButton.assertIsNotEnabled()
    }

    @Test
    fun clicking_on_minus_button_disables_the_plus_button(){
        assertThat(paymentAmountState.increase).isNull()
        val plusButton = composeTestRule.onNodeWithContentDescription("increase").assertIsEnabled()
        val minusButton = composeTestRule.onNodeWithContentDescription("decrease").assertIsEnabled()
        minusButton.performClick().assertIsEnabled()
        assertThat(paymentAmountState.increase).isEqualTo(false)
        plusButton.assertIsNotEnabled()
    }


}