package com.example.myapplication.data.student

import androidx.paging.PagingData
import com.example.myapplication.data.room.dao.NameWithFeeDate
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction
import com.example.myapplication.ui.home.model.SortField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StudentRepository {

    val upcomingDays: StateFlow<Int>

    fun onChangeUpcomingDays(days:Int)

    val upcomingStudents : Flow<PagingData<NameWithFeeDate>>

    fun allStudent(pageSize: Int, sortField: SortField, ascending:Boolean): Flow<PagingData<NameWithFeeDate>>


    suspend fun student(studentId: Long): Student

     fun transactionsForStudent(studentId: Long, pageSize: Int):Flow<PagingData<Transaction>>

     suspend fun getPendingAmount(studentId:Long):Int
     suspend fun insertStudent(student: Student)

    suspend fun updateStudent(student: Student)

    suspend fun deleteStudent(student: Student)

    suspend fun insertTransaction(transaction: Transaction)


    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun insertFeeHistory(feeHistory: FeeHistory)

     suspend fun updateFeeHistory(feeHistory: FeeHistory)

     suspend fun deleteFeeHistory(feeHistory: FeeHistory)

     fun anyPendingTransaction(studentId:Long):Flow<Boolean>


     fun pendingStudents(pageSize:Int):Flow<PagingData<NameWithFeeDate>>

}