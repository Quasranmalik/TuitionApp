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

    @Query("SELECT * FROM FeeHistory WHERE  student_id =:sid AND join_date = " +
            "(SELECT MAX(join_date) FROM FeeHistory WHERE student_id = :sid AND join_date <= :lastPaidDate)")
    suspend fun currentFeeHistory(sid:Long, lastPaidDate: LocalDate): FeeHistory

    @Query("SELECT * FROM FeeHistory WHERE   FeeHistory.student_id = :sid   AND " +
            "join_date > (SELECT MAX(paid_till_date) FROM Transactions WHERE :sid = Transactions.student_id )  " )
    suspend fun feeHistoryAfterLastPaidDate(sid:Long):List<FeeHistory>

    @Query("SELECT * FROM FeeHistory WHERE   FeeHistory.student_id = :sid   AND " +
            "join_date > :lastPaidDate  " )
    suspend fun feeHistoryAfterLastPaidDate(sid:Long, lastPaidDate: LocalDate):List<FeeHistory>




}