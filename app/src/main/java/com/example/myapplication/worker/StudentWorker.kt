package com.example.myapplication.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.KEY_OPERATION_TYPE
import com.example.myapplication.Operation
import com.example.myapplication.data.room.dao.StudentDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StudentWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val studentDao: StudentDao
    ): CoroutineWorker(context,params) {

    override  suspend fun doWork()=try{
        val student = inputData.toStudent()
        when(inputData.getInt(KEY_OPERATION_TYPE,-1)){
            Operation.INSERT.ordinal ->         studentDao.insert(student)
            Operation.UPDATE.ordinal -> studentDao.update(student)
            Operation.DELETE.ordinal -> studentDao.delete(student)
            else -> throw IllegalArgumentException("Invalid Operation Type Provided")
        }
        Result.success()
    }
    catch (throwable:Throwable){
        Result.failure()
    }
}