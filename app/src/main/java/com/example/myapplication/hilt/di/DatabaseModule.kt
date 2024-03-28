package com.example.myapplication.hilt.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.room.StudentDatabase
import com.example.myapplication.data.room.dao.FeeHistoryDao
import com.example.myapplication.data.room.dao.StudentDao
import com.example.myapplication.data.room.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStudentDao(database: StudentDatabase): StudentDao =
        database.studentDao()

    @Provides
    @Singleton
    fun provideFeeHistoryDao(database: StudentDatabase): FeeHistoryDao =
        database.feeHistoryDao()

    @Provides
    @Singleton
    fun provideTransactionDao(database: StudentDatabase): TransactionDao =
        database.transactionDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext:Context): StudentDatabase =
         Room.databaseBuilder(appContext, StudentDatabase::class.java,"student.db").build()



}