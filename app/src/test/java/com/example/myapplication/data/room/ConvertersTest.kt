package com.example.myapplication.data.room

import org.assertj.core.api.Assertions.*

import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ConvertersTest {
    val string:String = "2023-02-01"
    val str = "01022023"
    val format = DateTimeFormatter.ofPattern("ddMMyyyy")
    val date:LocalDate = LocalDate.parse(str,format)


    @Test
    fun fromStringToDate() {
        assertThat(date).isEqualTo(string)
    }

    @Test
    fun fromDateToString() {
        assertThat(date.toString()).isEqualTo(string)
    }
}