package com.example.myapplication.data.room.dao


import androidx.annotation.RestrictTo
import androidx.paging.PagingSource
import androidx.room.*
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import java.time.LocalDate

private const val studentListQuery = "Select id,first_name ,last_name,class,pending_months, " +
        "CASE "+
        "WHEN pending_months = 0 THEN"+
        "(SELECT MAX(paid_till_date) FROM TRANSACTIONS WHERE student_id = id)  " +
        "ELSE "+
        "(SELECT MAX(join_date) FROM FeeHistory WHERE student_id = id)"+
        "END as lastPaidDate "+
        "FROM Students "

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


   @Query( studentListQuery + "ORDER BY first_name ASC,last_name ASC")
   fun allStudentsWithLastPaidDateNameAscending():PagingSource<Int, NameWithFeeDate>

    @Query(studentListQuery + "ORDER BY first_name DESC,last_name DESC")
    fun allStudentsWithLastPaidDateNameDescending():PagingSource<Int, NameWithFeeDate>

    @Query( studentListQuery + "ORDER BY class ASC")
    fun allStudentsWithLastPaidDateClassAscending():PagingSource<Int, NameWithFeeDate>

    @Query(studentListQuery + "ORDER BY class DESC")
    fun allStudentsWithLastPaidDateClassDescending():PagingSource<Int, NameWithFeeDate>

   @RestrictTo(RestrictTo.Scope.TESTS)
    @Query("Select * FROM Students JOIN FeeHistory ON  Students.id = FeeHistory.student_id Where Students.id =:sid AND " +
                " join_date =  (SELECT  MAX(join_date) FROM  FeeHistory WHERE FeeHistory.student_id = :sid AND join_date <= " +
                            "(SELECT MAX(paid_till_date) FROM Transactions WHERE :sid = Transactions.student_id )) " )
    fun studentCurrentFeeHistory(sid:Long):Map<Student, FeeHistory>
}


//(SELECT  MAX(join_date) FROM  FeeHistory WHERE FeeHistory.student_id = :sid AND join_date <= )
//
//(SELECT MAX(paid_till_date) FROM Transactions WHERE :sid = Transactions.student_id )


data class NameWithFeeDate(
    val id:Long,
    @ColumnInfo(name="first_name") val firstName:String,
    @ColumnInfo(name="last_name") val lastName :String?,
    @ColumnInfo(name="pending_months") val pendingMonths:Int,
    @ColumnInfo(name = "class") val classYear:Int,
     val feeDate: LocalDate
)

