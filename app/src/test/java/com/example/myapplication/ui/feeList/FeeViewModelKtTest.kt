package com.example.myapplication.ui.feeList

import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.ui.fee.calculateAdvanceMonth
import com.example.myapplication.ui.fee.calculatePendingMonth
import com.example.myapplication.ui.model.PaymentMonth
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

class FeeViewModelKtTest {

    private val joinDate = LocalDate.of(2020, Month.JANUARY,1)

    private val feeHistory = FeeHistory(sid =1,joinDate = joinDate,fee = 100 )

    private val lastPaidDate = joinDate.withMonth(2)
    private val today = joinDate.withMonth(8).withDayOfMonth(10)
    private val pendingFeeHistory = listOf(
        feeHistory,
        feeHistory.copy(joinDate = joinDate.withMonth(3),fee=200),
        feeHistory.copy(joinDate = joinDate.withMonth(5),fee=300),

    )
    private val paymentMonth = PaymentMonth(fee=100,day=1,month = YearMonth.of(2020,3))

    private val expectedPendingMonths = listOf(
        paymentMonth,
        paymentMonth.copy(fee=200,month = YearMonth.of(2020,4)),
        paymentMonth.copy(fee=200,month = YearMonth.of(2020,5)),
        paymentMonth.copy(fee=300,month = YearMonth.of(2020,6)),
        paymentMonth.copy(fee=300,month = YearMonth.of(2020,7)),
        paymentMonth.copy(fee=300,month = YearMonth.of(2020,8)),



        )
    @Test
    fun calculatePendingMonthsTest() {
        assertThat(
            calculatePendingMonth(
            lastPaidDate = lastPaidDate,
            pendingFeeHistory = pendingFeeHistory,
            today = today
        )
        ).isEqualTo(expectedPendingMonths)
    }


}

class FeeViewModelKt2T{
    private val today = LocalDate.of(2020, Month.JANUARY,1)
    private val feeHistory = FeeHistory(sid =1,joinDate = today,fee = 100 )
    private val advanceFeeHistory = listOf(
        feeHistory,
        feeHistory.copy(joinDate = today.withMonth(3),fee=200),
        feeHistory.copy(joinDate = today.withMonth(5),fee=300),
        feeHistory.copy(joinDate = today.withMonth(6),fee=400),
        )

    private val expectedMonths = listOf(
        PaymentMonth(fee=100,day = 1,month = YearMonth.of(2020,Month.FEBRUARY)),
        PaymentMonth(fee=100,day = 1,month = YearMonth.of(2020,Month.MARCH)),
        PaymentMonth(fee=200,day = 1,month = YearMonth.of(2020,Month.APRIL)),
        PaymentMonth(fee=200,day = 1,month = YearMonth.of(2020,Month.MAY)),
        PaymentMonth(fee=300,day = 1,month = YearMonth.of(2020,Month.JUNE)),


    )

    @Test
    fun calculate_advance_month_test(){
        assertThat(calculateAdvanceMonth(feeHistory,advanceFeeHistory))
            .isEqualTo(expectedMonths)

        assertThat(calculateAdvanceMonth(feeHistory, emptyList(),today))
            .isEqualTo(List(5){PaymentMonth(fee =100,day=1,YearMonth.from(today).plusMonths(it.toLong()+1))})
    }


}