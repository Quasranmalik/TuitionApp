package com.example.myapplication.ui.home

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.example.myapplication.data.room.dao.NameWithFeeDate
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction
import com.example.myapplication.data.student.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class FakeStudentRepository1(private val studentsNameAndDate:List<NameWithFeeDate>): StudentRepository {


    override fun allStudent(
        pageSize: Int,
        sortField: SortField,
        ascending: Boolean
    ): Flow<PagingData<NameWithFeeDate>> = flowOf(
        PagingData.from(
            studentsNameAndDate,
            LoadStates(
                refresh = LoadState.NotLoading(true),
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(true)

            )
        )
    )
    suspend fun pendingFeeMonthHistoryOfStudent(sid: Long): List<FeeHistory> {
        TODO("Not yet implemented")
    }


    override suspend fun student(studentId: Long): Student {
        TODO("Not yet implemented")
    }

    override fun transactionsForStudent(studentId: Long, pageSize: Int): Flow<PagingData<Transaction>> {
        TODO("Not yet implemented")
    }

    override suspend fun lastPaidDateForStudent(studentId: Long): LocalDate {
        TODO("Not yet implemented")
    }

    override suspend fun getPendingAmount(studentId: Long): Int {
        TODO("Not yet implemented")
    }

    override suspend fun insertStudent(student: Student) {
        TODO("Not yet implemented")
    }

    override suspend fun updateStudent(student: Student) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteStudent(student: Student) {
        TODO("Not yet implemented")
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        TODO("Not yet implemented")
    }

    override suspend fun insertFeeHistory(feeHistory: FeeHistory) {
        TODO("Not yet implemented")
    }

    override suspend fun pendingFeeHistory(studentId: Long): List<FeeHistory> {
        TODO("Not yet implemented")
    }

    override suspend fun pendingFeeHistory(
        studentId: Long,
        lastPaidDate: LocalDate?
    ): List<FeeHistory> {
        TODO("Not yet implemented")
    }

    override suspend fun advanceFeeHistory(studentId: Long): List<FeeHistory> {
        TODO("Not yet implemented")
    }

    override suspend fun updateFeeHistory(feeHistory: FeeHistory) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFeeHistory(feeHistory: FeeHistory) {
        TODO("Not yet implemented")
    }

    override fun anyPendingTransaction(studentId: Long): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun upcomingStudents(
        withinDays: Int,
        pageSize: Int
    ): Flow<PagingData<NameWithFeeDate>> {
        TODO("Not yet implemented")
    }

    override fun pendingStudents(pageSize: Int): Flow<PagingData<NameWithFeeDate>> {
        TODO("Not yet implemented")
    }




}





//val expectedStudentSortedByName = NameWithPendingMonth.StudentItem(id=1,name="AA",classYear=1, pendingMonths = 2)
