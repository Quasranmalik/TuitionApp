package com.example.myapplication

import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import java.time.Period
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@HiltAndroidTest
class TimePeriodTest {
    val today = LocalDate.now()


    @Test
    fun checkMonthsBetweenDates(){
        val date = LocalDate.of(2023,1,3)
        assertThat(Period.between(date,today).months).isEqualTo(7)
    }

    @Test
    fun dateTimeFormatterTest(){
        val date = LocalDate.now()
        val formatter2 = DateTimeFormatter.ofPattern("MMM")

        assertThat(date.format(formatter2)).isEqualTo("Nov")
    }


    @Test
    fun year_month_format_test(){
        YearMonth.now().format(DateTimeFormatter.ofPattern("MMM yyyy"))
            .let { assertThat(it).isEqualTo("November 2023") }


    }


}