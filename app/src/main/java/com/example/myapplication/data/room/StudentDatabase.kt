package com.example.myapplication.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.myapplication.data.room.dao.FeeHistoryDao
import com.example.myapplication.data.room.dao.StudentDao
import com.example.myapplication.data.room.dao.TransactionDao
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction

import java.time.LocalDate


@TypeConverters(Converters::class)
@Database(entities = [Student::class, FeeHistory::class, Transaction::class],version = 1)
abstract class StudentDatabase : RoomDatabase(){
    abstract fun studentDao() : StudentDao
    abstract fun transactionDao(): TransactionDao
    abstract fun feeHistoryDao(): FeeHistoryDao
}

  class Converters{


    @TypeConverter
    fun fromEpochDay(day : Long?):LocalDate?{
        return  day?.let { LocalDate.ofEpochDay(it) }
    }

     @TypeConverter
    fun fromDateToLong(date : LocalDate?):Long?{
        return date?.toEpochDay()
    }
}