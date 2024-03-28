package com.example.myapplication.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDate

@Entity(foreignKeys = [ForeignKey(entity= Student::class, parentColumns = ["id"], childColumns = ["student_id"])],
    primaryKeys = ["student_id","join_date"])
data class FeeHistory (
  @ColumnInfo(name ="student_id") val sid: Long,
  @ColumnInfo(name ="join_date") val joinDate : LocalDate,
  val fee:Int
)