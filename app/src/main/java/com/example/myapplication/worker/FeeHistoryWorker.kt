package com.example.myapplication.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.KEY_OPERATION_TYPE
import com.example.myapplication.Operation
import com.example.myapplication.data.room.dao.FeeHistoryDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FeeHistoryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val feeHistoryDao: FeeHistoryDao
): CoroutineWorker(context,params) {

    override  suspend fun doWork()=try{
        val feeHistory = inputData.toFeeHistory()
        when(inputData.getInt(KEY_OPERATION_TYPE,-1)){
            Operation.INSERT.ordinal ->         feeHistoryDao.insert(feeHistory)
            Operation.UPDATE.ordinal -> feeHistoryDao.update(feeHistory)
            Operation.DELETE.ordinal -> feeHistoryDao.delete(feeHistory)
            else -> throw IllegalArgumentException("Invalid Operation Type Provided")
        }
        Result.success()
    }
    catch (throwable:Throwable){
        Result.failure()
    }
}