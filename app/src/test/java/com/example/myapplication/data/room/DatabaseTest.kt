//package com.example.myapplication.database
//
//import android.content.Context
//import androidx.paging.PagingConfig
//import androidx.paging.PagingSource
//import androidx.paging.testing.TestPager
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import com.example.myapplication.database.dao.StudentDao
//import com.example.myapplication.database.model.NameTuple
//import com.example.myapplication.database.model.Student
//import kotlinx.coroutines.test.runTest
//import org.assertj.core.api.Assertions.*
//
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.annotation.Config
//
//import java.time.LocalDate
//
//@RunWith(RobolectricTestRunner::class)
//@Config(sdk = [28])
// class StudentDatabaseTest {
//    private lateinit var studentDao: StudentDao
//    private lateinit var db : StudentDatabase
//    private val date : LocalDate = LocalDate.parse("2023-01-01")
//
//    private val mockStudent = listOf(NameTuple("Quasran",true),
//        NameTuple("Shankar",true),
//        NameTuple("Amit",true)
//    )
//
//    @Before
//    fun createDb(){
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = Room.inMemoryDatabaseBuilder(context,StudentDatabase::class.java)
//            .allowMainThreadQueries().build()
//        studentDao= db.studentDao()
//        studentDao.insertStudent(students[0])
////        students.forEach { studentDao.insertStudent(it) }
//    }
//    @Test
//    fun paging_source_check() = runTest{
//        students.forEach { studentDao.insertStudent(it) }
//        val pagingSource = studentDao.loadAllStudents()
//        val pager = TestPager(PagingConfig(pageSize = 2, initialLoadSize = 3),pagingSource)
//        val result = pager.refresh() as PagingSource.LoadResult.Page
//
//        assertThat(result.data).containsExactlyElementsOf(mockStudent)
//
//
//    }
//
//    @Test
//    fun BuildCheck(){
//        assertThat(studentDao.loadStudents()[0]).hasFieldOrPropertyWithValue("pending",false)
//    }
//
//    @Test
//    fun default_value_check(){
//        studentDao.insertStudent(students[0])
//    }
//
//}
//
//val students = listOf(
//    Student(1,"Quasran",10),
//    Student(2,"Shankar",10,true),
//    Student(3,"Amit",10,true),
//    Student(4,"Suraj",10,true),
//    Student(5,"Aman",10,true) )