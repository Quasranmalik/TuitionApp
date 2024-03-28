package com.example.myapplication.data.room.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDate



@Entity(foreignKeys = [ForeignKey(entity= Student::class, parentColumns = ["id"],childColumns=["student_id"])],
    tableName = "Transactions", primaryKeys = ["student_id","paid_till_date"])
data class Transaction(

    @ColumnInfo(name="student_id") val sid:Long,
    @ColumnInfo(name = "paid_till_date") val paidTillDate:LocalDate,
    val month:Int,
    val discount:Int =0
)