package com.example.myapplication.ui.home

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.example.myapplication.data.room.dao.NameWithPaidTillDate
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction
import com.example.myapplication.data.student.StudentRepository
import com.example.myapplication.model.NameWithPendingMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class FakeStudentRepository1(private val studentsNameAndDate:List<NameWithPaidTillDate>): StudentRepository {


    override fun allStudentWithPendingMonths(
        pageSize: Int,
        sortField: SortField,
        ascending: Boolean
    ): Flow<PagingData<NameWithPaidTillDate>> = flowOf(
        PagingData.from(
            studentsNameAndDate,
            LoadStates(
                refresh = LoadState.NotLoading(true),
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(true)

            )
        )
    )
    override suspend fun pendingFeeMonthHistoryOfStudent(sid: Long): List<FeeHistory> {
        TODO("Not yet implemented")
    }


    override suspend fun student(sid: Long): Student {
        TODO("Not yet implemented")
    }

    override fun transactionsForStudent(sid: Long, pageSize: Int): Flow<PagingData<Transaction>> {
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

    override suspend fun updateFeeHistory(feeHistory: FeeHistory) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFeeHistory(feeHistory: FeeHistory) {
        TODO("Not yet implemented")
    }

    override fun anyPendingTransaction(id: Long): Flow<Boolean> {
        TODO("Not yet implemented")
    }

}
val date= LocalDate.now().minusMonths(2)
val student2 = NameWithPaidTillDate(id=1,firstName="class",lastName="",classYear=1,lastPaidDate=date)

val studentsSortedByClass = listOf(student2,
    student2,student2.copy(classYear=2),student2.copy(classYear=2))



//val expectedStudentSortedByName = NameWithPendingMonth.StudentItem(id=1,name="AA",classYear=1, pendingMonths = 2)
