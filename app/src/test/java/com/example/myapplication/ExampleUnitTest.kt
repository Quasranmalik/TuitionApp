package com.example.myapplication

import org.junit.Test
import org.assertj.core.api.Assertions.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {



    @Test
    fun epochSeconds_are_same() {
        val actual =with(LocalDate.now()){
            this.minusDays(dayOfMonth.toLong()-1)
            this.atStartOfDay(ZoneId.of("UTC")).toEpochSecond()
        }
        val expected=ZonedDateTime.of(2023,7,1,0,0,0,0,ZoneId.of("UTC")).toEpochSecond()
        assertThat(actual).isEqualTo(expected)

    }

    @Test
    fun zoned_Date_Time_are_same(){
//        val actual =firstDayofMo().atStartOfDay(ZoneId.of("UTC")).toEpochSecond()
        val expected=ZonedDateTime.of(2023,7,1,0,0,0,0,ZoneId.of("UTC")).toEpochSecond()
        assertThat(test_fun()).isEqualTo(expected)
    }

    @Test
    fun date_are_same(){


       assertThat(firstDayofMo().toString()).isEqualTo("2023-07-01")


    }

    private fun firstDayofMo() : LocalDate=with(LocalDate.now()){
        this.minusDays(dayOfMonth.toLong()-1)

    }

    private fun test_fun() =  with(LocalDate.now()){
        this.minusDays(dayOfMonth.toLong()-1).atStartOfDay(ZoneId.of("UTC")).toEpochSecond()

    }




}