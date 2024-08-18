package com.example.myapplication.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.room.model.FeeHistory
import java.time.LocalDate

@Dao
interface FeeHistoryDao {
    @Insert
    fun insertFeeHistory(feeHistory : FeeHistory)

    @Insert
    suspend fun insert(feeHistory: FeeHistory)

    @Update
    suspend fun update(feeHistory: FeeHistory)

    @Update
    suspend fun delete(feeHistory: FeeHistory)

    @Query("SELECT * FROM FeeHistory WHERE  student_id =:studentId AND join_date = " +
            "(SELECT MAX(join_date) FROM FeeHistory WHERE student_id = :studentId AND join_date <= :lastPaidDate)")
    suspend fun currentFeeHistory(studentId:Long, lastPaidDate: LocalDate): FeeHistory

    @Query("SELECT * FROM FeeHistory WHERE  student_id =:studentId AND join_date = " +
            "(SELECT MAX(join_date) FROM FeeHistory WHERE student_id = :studentId AND " +
            "join_date <= (SELECT MAX(paid_till_date) FROM Transactions WHERE :studentId = Transactions.student_id ))")
    suspend fun feeHistoryOnLastPaidDate(studentId: Long):FeeHistory
    @Query("SELECT * FROM FeeHistory WHERE student_id = :studentId AND" +
            "  (SELECT MAX(paid_till_date) FROM Transactions WHERE :studentId = Transactions.student_id ) < join_date AND " +
            "join_date <= CAST(strftime('%s',:today * 24*60*60,'unixepoch','-1 month')  AS INT ) /(24*60*60) ")
    suspend fun pendingFeeHistoryAfterLastPaidDate(studentId:Long, today:LocalDate):List<FeeHistory>
    @Query("SELECT * FROM FeeHistory WHERE student_id = :studentId AND" +
            "  :lastPaidDate < join_date AND " +
            "join_date <= CAST(strftime('%s',:today * 24*60*60,'unixepoch','-1 month')  AS INT ) /(24*60*60) ")
    suspend fun pendingFeeHistoryAfterLastPaidDate(studentId:Long, today:LocalDate, lastPaidDate: LocalDate):List<FeeHistory>
    @Query("SELECT * FROM FeeHistory WHERE FeeHistory.student_id = :studentId AND " +
            " join_date >  CAST(strftime('%s',:today * 24*60*60,'unixepoch','-1 month')  AS INT ) /(24*60*60)")
    suspend fun advanceFeeHistory(studentId:Long, today:LocalDate):List<FeeHistory>
    @Query("SELECT * FROM FeeHistory WHERE   FeeHistory.student_id = :studentId   AND " +
            "join_date > (SELECT MAX(paid_till_date) FROM Transactions WHERE :studentId = Transactions.student_id )  " )
    suspend fun feeHistoryAfterLastPaidDate(studentId:Long):List<FeeHistory>

    @Query("SELECT * FROM FeeHistory WHERE   FeeHistory.student_id = :studentId   AND " +
            "join_date > :lastPaidDate  " )
    suspend fun feeHistoryAfterLastPaidDate(studentId:Long, lastPaidDate: LocalDate):List<FeeHistory>





}

