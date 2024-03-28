package com.example.myapplication.data.student

import androidx.annotation.RestrictTo
import androidx.annotation.VisibleForTesting
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.myapplication.data.room.dao.NameWithPaidTillDate
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction
import com.example.myapplication.ui.home.SortField
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun allStudentWithPendingMonths(pageSize: Int, sortField: SortField, ascending:Boolean): Flow<PagingData<NameWithPaidTillDate>>

    suspend fun pendingFeeMonthHistoryOfStudent(sid: Long): List<FeeHistory>

    suspend fun student(sid: Long): Student

     fun transactionsForStudent(sid: Long,pageSize: Int):Flow<PagingData<Transaction>>

     suspend fun insertStudent(student: Student)

    suspend fun updateStudent(student: Student)

    suspend fun deleteStudent(student: Student)

    suspend fun insertTransaction(transaction: Transaction)


    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun insertFeeHistory(feeHistory: FeeHistory)

     suspend fun updateFeeHistory(feeHistory: FeeHistory)

     suspend fun deleteFeeHistory(feeHistory: FeeHistory)

     fun anyPendingTransaction(id:Long):Flow<Boolean>

}