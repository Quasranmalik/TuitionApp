package com.example.myapplication.data.room.model

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.data.room.StudentDatabase
import com.example.myapplication.data.room.dao.StudentDao
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.dao.TransactionDao

import org.junit.Before

class TransactionTest {

    private lateinit var db : StudentDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var studentDao: StudentDao
    private val student = Student(id=1,firstName = "Quasran", classYear = 10)

    @Before
    fun setUp() {
                val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, StudentDatabase::class.java)
            .allowMainThreadQueries().build()
        studentDao = db.studentDao()
        transactionDao = db.transactionDao()

    }

//    @Test
//    fun check_transaction_insertion_and_retrieval(){
//        studentDao.insertStudent(student)
//        val transaction1=Transaction(sid=student.id, paidTillDate = LocalDate.now(),month=4)
//        val transaction2 =Transaction(sid=student.id, paidTillDate = LocalDate.of(2022,4,4),month=4)
//        transactionDao.insertTransaction(transaction1)
//        transactionDao.insertTransaction(transaction2)
//        assertThat(studentDao.loadTransactionForStudent(student.id).transactions).isNotEmpty().hasSize(2).containsExactlyInAnyOrder(transaction1,transaction2)
//    }
}