package com.example.myapplication.data.room

import android.content.Context
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.room.dao.FeeHistoryDao
import com.example.myapplication.data.room.dao.NameWithFeeDate
import com.example.myapplication.data.room.dao.StudentDao
import com.example.myapplication.data.room.dao.TransactionDao
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
@RunWith(AndroidJUnit4::class)
class StudentDaoUpcomingStudentTest {

    private lateinit var db : StudentDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var studentDao: StudentDao
    private lateinit var feeHistoryDao: FeeHistoryDao

    private val date = LocalDate.of(2020,1,1)

    private  val  students:List<Student> = List(10){index ->
        Student(id = (index+1).toLong(),firstName = (index+'A'.toInt()).toChar().toString(), classYear = 1, pendingMonths = 0)
    }
    private  val transactions:List<Transaction> =List(10){index ->
        Transaction(sid=(index+1).toLong(),
            paidTillDate = date.plusDays(index.toLong()),month=1 )

    }

    private val expectedStudents = listOf(
        NameWithFeeDate(id=0,firstName = "A",lastName = null,classYear = 1, pendingMonths =0, feeDate = date ),
        NameWithFeeDate(id=1,firstName = "B",lastName = null,classYear = 1, pendingMonths =0, feeDate = date.plusDays(1) ),
        NameWithFeeDate(id=2,firstName = "C",lastName = null,classYear = 1, pendingMonths =0, feeDate = date.plusDays(2) )
    )

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


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun student_upcoming_fees_test() = runTest{
        repeat(10){index ->
//            launch{
                studentDao.insert(students[index])
                transactionDao.insert(transactions[index])
//            }
        }
//        advanceUntilIdle()
        val pager = TestPager(PagingConfig(pageSize = 10, initialLoadSize = 10),studentDao.studentUpcoming(1,3))
        val result = pager.refresh() as PagingSource.LoadResult.Page

        assertThat(result.data).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactlyInAnyOrderElementsOf(expectedStudents)



    }

}