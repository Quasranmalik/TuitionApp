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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StudentDataBaseTest {

    private lateinit var db : StudentDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var studentDao: StudentDao
    private lateinit var feeHistoryDao: FeeHistoryDao
    private val transactions = run{
        val transaction = Transaction(sid =1,paidTillDate = LocalDate.of(2021,1,1),month=4)
        listOf(transaction,transaction.copy(paidTillDate = LocalDate.of(2022,1,1)))
    }

    private val feeHistories = run{
        val feeHistory = FeeHistory(sid=1,joinDate= LocalDate.of(2020,1,1),fee=100)
        listOf(feeHistory,feeHistory.copy(joinDate = LocalDate.of(2021,1,1)),
            feeHistory.copy(joinDate = LocalDate.of(2022,1,1)),
            feeHistory.copy(joinDate = LocalDate.of(2023,1,1)))
    }
    private val student = Student(id=1,firstName = "Quasran", classYear = 10, pendingMonths = 0)

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
    private suspend  fun insert_data(){
        studentDao.insert(student)
        transactions.forEach { transactionDao.insert(it) }
        feeHistories.forEach { feeHistoryDao.insertFeeHistory(it) }
    }



    @Test
    fun check_student_dao_load_all_students_with_pending_month_0_returns_last_transaction_test() = runTest {
        insert_data()
        val studentPagingSource = studentDao.allStudentsWithLastPaidDateNameAscending()
        val pager = TestPager(PagingConfig(pageSize = 4, initialLoadSize = 4),studentPagingSource)
        val result = pager.refresh() as PagingSource.LoadResult.Page
        assertThat(result.data).singleElement().
        extracting("lastPaidDate").isEqualTo(LocalDate.of(2022,1,1))


    }
    @Test
    fun check_student_dao_load_all_students_with_pending_month_0_returns_last_fee_history_test() = runTest{
        insert_data()
        studentDao.update(student.copy(pendingMonths = 1))
        val studentPagingSource = studentDao.allStudentsWithLastPaidDateNameAscending()
        val pager = TestPager(PagingConfig(pageSize = 4, initialLoadSize = 4),studentPagingSource)
        val result = pager.refresh() as PagingSource.LoadResult.Page
        assertThat(result.data).singleElement().
        extracting("feeDate").isEqualTo(LocalDate.of(2023,1,1))
    }

    @Test
    fun student_dao_load_transaction_for_student_test() =runTest {
        insert_data()
        val studentPagingSource = transactionDao.forStudent(1)
        val pager = TestPager(PagingConfig(pageSize = 4, initialLoadSize = 4),studentPagingSource)
        val result = pager.refresh() as PagingSource.LoadResult.Page
        assertThat(result.data).
        hasSize(2).containsExactlyInAnyOrderElementsOf(transactions)

    }

    @Test
    fun student_dao_load_student_test()=runTest{
        insert_data()
// studentDao.loadStudent loads the entry of feeHistory corresponding to fees currently applicable
        assertThat(studentDao.studentCurrentFeeHistory(sid=1)[student]?.joinDate).isEqualTo(LocalDate.of(2022,1,1))

    }

    @Test
    fun student_dao_load_fee_history_after_last_paid_date() =runTest{
        insert_data()
        launch{
            assertThat(feeHistoryDao.feeHistoryAfterLastPaidDate(sid=1)).hasSize(1).first()
            .isEqualTo(feeHistories[3]) }
        launch{
            assertThat(feeHistoryDao.feeHistoryAfterLastPaidDate(sid=1, lastPaidDate = LocalDate.of(2022,1,1))).hasSize(1).first()
                .isEqualTo(feeHistories[3])
        }

        launch{
            assertThat(feeHistoryDao.feeHistoryAfterLastPaidDate(sid=1, lastPaidDate = LocalDate.of(2021,1,1)))
                .containsExactlyInAnyOrderElementsOf(feeHistories.subList(2,4))
        }
    }

    @Test
    fun student_dao_load_current_fee_history() = runTest{
        insert_data()

        runBlocking {
            assertThat(feeHistoryDao.currentFeeHistory(sid=1, lastPaidDate = LocalDate.of(2022,1,1))).
            isEqualTo(feeHistories[2])
        }

    }

    @Test
    fun fee_history_dao_last_paid_date()=runTest{
        insert_data()
        assertThat(transactionDao.lastPaidDateForStudent(sid=1)).isEqualTo(LocalDate.of(2022,1,1))
    }


}