package com.example.myapplication.ui.util


import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate

class UtilsKtTest {

    val startDate = LocalDate.of(2020,6,15)
    val feeDate = LocalDate.of(2020,3,1)

    @Test
    fun upcomingDays() {

        assertThat(upcomingDays(startDate,feeDate)).isEqualTo(16)

    }
}