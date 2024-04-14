package com.example.myapplication.data.room

import android.content.Context
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.room.dao.FeeHistoryDao
import com.example.myapplication.data.room.dao.StudentDao
import com.example.myapplication.data.room.dao.TransactionDao
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
@RunWith(AndroidJUnit4::class)
class StudentDaoPendingStudentTest {

    private lateinit var db : StudentDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var studentDao: StudentDao
    private lateinit var feeHistoryDao: FeeHistoryDao

    private val students = listOf(Student(id=1,firstName="A",classYear=1,
        pendingMonths = 0),
        Student(id=2,firstName="B",classYear=1, pendingMonths = 1),
        Student(id=3,firstName="C",classYear=1, pendingMonths = 0))

    private val transactions = listOf(
        Transaction(sid=1, paidTillDate = LocalDate.now().minusMonths(1),month=1),
        Transaction(sid=2,paidTillDate = LocalDate.now().minusDays(15),month=1),
        Transaction(sid=3,paidTillDate = LocalDate.now().minusDays(15),month=1))

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, StudentDatabase::class.java)
            .allowMainThreadQueries().build()
        studentDao = db.studentDao()
        transactionDao = db.transactionDao()
        feeHistoryDao= db.feeHistoryDao()

    }

    @After
    fun closeDb(){
        db.close()
    }

    private suspend fun insert_data(){
        students.forEach { studentDao.insert(it) }
        transactions.forEach { transactionDao.insert(it) }
        feeHistoryDao.insert(FeeHistory(sid=2, joinDate = LocalDate.now(),fee=100))
    }
    @Test
    fun student_dao_pending_student_test() = runTest{
        insert_data()
        val pager = TestPager(PagingConfig(pageSize = 3, initialLoadSize = 3),studentDao.pendingStudents())
        val result = pager.refresh() as PagingSource.LoadResult.Page
        assertThat(result.data).extracting("id").containsExactlyInAnyOrder(1L,2L)

    }
}