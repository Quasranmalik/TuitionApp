package com.example.myapplication.data.room.dao


import androidx.annotation.RestrictTo
import androidx.paging.PagingSource
import androidx.room.*
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import java.time.LocalDate

@Dao
interface StudentDao {

    @Query("SELECT * FROM Students WHERE id = :sid")
    suspend fun student(sid:Long) : Student

    @Delete
    suspend fun delete(student : Student)

    @Insert
    suspend fun insert(student: Student)

    @Update
    suspend fun update(student: Student)


   @Query("Select id,first_name ,last_name,class,MAX(paid_till_date) as lastPaidDate from Students JOIN Transactions" +
           " ON Students.id = Transactions.student_id GROUP BY id " +
           "ORDER BY first_name ASC,last_name ASC")
   fun allStudentsWithLastPaidDateNameAscending():PagingSource<Int, NameWithPaidTillDate>

    @Query("Select id,first_name ,last_name,class,MAX(paid_till_date) as lastPaidDate from Students JOIN Transactions" +
            " ON Students.id = Transactions.student_id GROUP BY id " +
            "ORDER BY first_name DESC,last_name DESC")
    fun allStudentsWithLastPaidDateNameDescending():PagingSource<Int, NameWithPaidTillDate>

    @Query("Select id,first_name ,last_name,class,MAX(paid_till_date) as lastPaidDate from Students JOIN Transactions" +
            " ON Students.id = Transactions.student_id GROUP BY id " +
            "ORDER BY class ASC")
    fun allStudentsWithLastPaidDateClassAscending():PagingSource<Int, NameWithPaidTillDate>

    @Query("Select id,first_name ,last_name,class,MAX(paid_till_date) as lastPaidDate from Students JOIN Transactions" +
            " ON Students.id = Transactions.student_id GROUP BY id " +
            "ORDER BY class DESC")
    fun allStudentsWithLastPaidDateClassDescending():PagingSource<Int, NameWithPaidTillDate>

   @RestrictTo(RestrictTo.Scope.TESTS)
    @Query("Select * FROM Students JOIN FeeHistory ON  Students.id = FeeHistory.student_id Where Students.id =:sid AND " +
                " join_date =  (SELECT  MAX(join_date) FROM  FeeHistory WHERE FeeHistory.student_id = :sid AND join_date <= " +
                            "(SELECT MAX(paid_till_date) FROM Transactions WHERE :sid = Transactions.student_id )) " )
    fun studentCurrentFeeHistory(sid:Long):Map<Student, FeeHistory>
}


//(SELECT  MAX(join_date) FROM  FeeHistory WHERE FeeHistory.student_id = :sid AND join_date <= )
//
//(SELECT MAX(paid_till_date) FROM Transactions WHERE :sid = Transactions.student_id )


data class NameWithPaidTillDate(
    val id:Long,
    @ColumnInfo(name="first_name") val firstName:String,
    @ColumnInfo(name="last_name") val lastName :String?,
    @ColumnInfo(name = "class") val classYear:Int,
     val lastPaidDate: LocalDate
)

