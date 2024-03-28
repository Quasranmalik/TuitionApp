package com.example.myapplication.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.room.model.Transaction
import java.time.LocalDate

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("Select * from   Transactions WHERE student_id=:sid ORDER BY paid_till_date DESC ")
    fun forStudent(sid:Long) : PagingSource<Int, Transaction>

    @Query("Select * from Transactions ORDER BY paid_till_date DESC")
    fun loadAllTransactions():PagingSource<Int, Transaction>

    @Query("Select MAX(paid_till_date) FROM Transactions WHERE student_id = :sid ")
    suspend fun lastPaidDateForStudent(sid:Long): LocalDate

}