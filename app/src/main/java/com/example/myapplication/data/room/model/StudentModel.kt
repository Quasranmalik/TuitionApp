package com.example.myapplication.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "Students")
data class Student(
    @PrimaryKey(autoGenerate = true)  val id:Long=0,
    @ColumnInfo(name = "first_name")  val firstName:String,
    @ColumnInfo(name = "last_name")   val lastName:String?=null,
    @ColumnInfo(name = "class")       val classYear: Int,
    @ColumnInfo(name="pending_months") val pendingMonths:Int

)




