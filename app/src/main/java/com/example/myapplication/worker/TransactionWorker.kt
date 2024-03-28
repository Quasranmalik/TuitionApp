package com.example.myapplication.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.KEY_OPERATION_TYPE
import com.example.myapplication.Operation
import com.example.myapplication.data.room.dao.TransactionDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TransactionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val transactionDao: TransactionDao
): CoroutineWorker(context,params) {

    override  suspend fun doWork()=try{
        val transaction = inputData.toTransaction()
        when(inputData.getInt(KEY_OPERATION_TYPE,-1)){
            Operation.INSERT.ordinal ->         transactionDao.insert(transaction)
            Operation.UPDATE.ordinal ->
                transactionDao.update(transaction)
            Operation.DELETE.ordinal -> transactionDao.delete(transaction)
            else -> throw IllegalArgumentException("Invalid Operation Type Provided")
        }
        Result.success()
    }
    catch (throwable:Throwable){
        Result.failure()
    }
}