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
        "END as fee_date "+
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
   fun allStudentsNameAscending():PagingSource<Int, NameWithFeeDate>

    @Query(studentListQuery + "ORDER BY first_name DESC,last_name DESC")
    fun allStudentsNameDescending():PagingSource<Int, NameWithFeeDate>

    @Query( studentListQuery + "ORDER BY class ASC")
    fun allStudentsClassAscending():PagingSource<Int, NameWithFeeDate>

    @Query(studentListQuery + "ORDER BY class DESC")
    fun allStudentsClassDescending():PagingSource<Int, NameWithFeeDate>

   @RestrictTo(RestrictTo.Scope.TESTS)
    @Query("SELECT * FROM Students JOIN FeeHistory ON  Students.id = FeeHistory.student_id Where Students.id =:sid AND " +
                " join_date =  (SELECT  MAX(join_date) FROM  FeeHistory WHERE FeeHistory.student_id = :sid AND join_date <= " +
                            "(SELECT MAX(paid_till_date) FROM Transactions WHERE :sid = Transactions.student_id )) " )
    fun studentCurrentFeeHistory(sid:Long):Map<Student, FeeHistory>

    @Query("Select id,first_name ,last_name,class,pending_months,fee_date," +
            "(CASE " +
            "WHEN CAST  (strftime('%d',fee_date*24*3600,'unixepoch') AS INT) >= :fromDay THEN " +
            "CAST  (strftime('%d',fee_date*24*3600,'unixepoch') AS INT)  " +
            "ELSE " +
            "(CAST  (strftime('%d',fee_date*24*3600,'unixepoch') AS INT) + 31 ) " +
            "END) AS fee_day "+
            "FROM  " +
            "(SELECT *, (CASE "+
            "WHEN pending_months = 0 THEN"+
            "(SELECT MAX(paid_till_date) FROM TRANSACTIONS WHERE student_id = id)  " +
            "ELSE "+
            "(SELECT MAX(join_date) FROM FeeHistory WHERE student_id = id) "+
            "END) AS fee_date FROM Students )  " +
            "WHERE fee_day BETWEEN :fromDay AND " +
            " (CASE WHEN :toDay >= :fromDay THEN :toDay ELSE 31 + :toDay END)" +
            "ORDER BY fee_day,first_name,last_name" )
    fun upcomingStudents(fromDay:Int, toDay:Int):PagingSource<Int,NameWithFeeDate>
    @Query(studentListQuery +
            " WHERE (pending_months > 0 OR CAST (strftime('%s','now','-1 month') AS INT) >= fee_date*24*60*60)" +
            "ORDER BY first_name ASC,last_name ASC")
    fun pendingStudents():PagingSource<Int,NameWithFeeDate>


}




data class NameWithFeeDate(
    val id:Long,
    @ColumnInfo(name="first_name") val firstName:String,
    @ColumnInfo(name="last_name") val lastName :String?,
    @ColumnInfo(name="pending_months") val pendingMonths:Int,
    @ColumnInfo(name = "class") val classYear:Int,
    @ColumnInfo(name = "fee_date") val feeDate: LocalDate
)

