package com.example.myapplication.data.room

import android.content.Context
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
class StudentDaoPendingFeeHistoryAfterLastPaidDateTest {

    private lateinit var db : StudentDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var studentDao: StudentDao
    private lateinit var feeHistoryDao: FeeHistoryDao

    val student = Student(id=1,firstName="A",classYear=1,
        pendingMonths = 0)

    private val feeHistory = List(12){ index ->
        FeeHistory(sid =1 ,joinDate = LocalDate.of(2020,index+1,1),fee = (index+1)*100)
    }

    private val lastPaidDate =LocalDate.of(2020,6,1)
    private val transaction = Transaction(sid =1 ,paidTillDate = lastPaidDate,month = 6)
    private val today = LocalDate.of(2020,10,1)

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

    private suspend fun insertData(){
        studentDao.insert(student)
        transactionDao.insert(transaction)
        feeHistory.forEach{feeHistoryDao.insert(it)}
    }

    @Test
    fun student_dao_pending_fee_history_after_last_paid_date_test() = runTest{
        insertData()
        assertThat(feeHistoryDao.pendingFeeHistoryAfterLastPaidDate(studentId =1, today = today))
            .isEqualTo(feeHistory.subList(6,9))
        assertThat(feeHistoryDao.pendingFeeHistoryAfterLastPaidDate(studentId =1, today = today, lastPaidDate = lastPaidDate))
            .isEqualTo(feeHistory.subList(6,9))
    }
    @Test
    fun student_dao_advance_fee_history() = runTest{
        insertData()
        assertThat(feeHistoryDao.advanceFeeHistory(studentId =1, today =today))
            .isEqualTo(feeHistory.subList(9,11))
    }

    @Test
    fun current_fee_history() = runTest{
        insertData()
        assertThat(feeHistoryDao.feeHistoryOnLastPaidDate(studentId =1)).
                isEqualTo(feeHistory[5])
        assertThat(feeHistoryDao.currentFeeHistory(studentId=1, lastPaidDate = lastPaidDate)).
        isEqualTo(feeHistory[5])
    }


}