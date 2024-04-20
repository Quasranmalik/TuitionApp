package com.example.myapplication.data.student.implementation

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.example.myapplication.Operation
import com.example.myapplication.data.student.StudentRepository
import com.example.myapplication.data.room.dao.FeeHistoryDao
import com.example.myapplication.data.room.dao.NameWithFeeDate
import com.example.myapplication.data.room.dao.StudentDao
import com.example.myapplication.data.room.dao.TransactionDao
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction
import com.example.myapplication.ui.home.SortField
import com.example.myapplication.worker.FeeHistoryWorker
import com.example.myapplication.worker.StudentWorker
import com.example.myapplication.worker.TransactionWorker
import com.example.myapplication.worker.toWorkData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class RoomStudentRepository @Inject constructor(@ApplicationContext context: Context, private val studentDao : StudentDao,
                                                private val feeHistoryDao: FeeHistoryDao,
                                                private val transactionDao: TransactionDao
) :
                                                StudentRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun allStudent(
        pageSize: Int,
        sortField: SortField,
        ascending: Boolean
    ): Flow<PagingData<NameWithFeeDate>> = Pager(
        config = PagingConfig(pageSize = pageSize)
        ){
        when(sortField){
            SortField.Name -> when(ascending){
                true -> studentDao.allStudentsNameAscending()
                false -> studentDao.allStudentsNameDescending()
            }
            SortField.Class -> when(ascending){
                true -> studentDao.allStudentsClassAscending()
                false -> studentDao.allStudentsClassDescending()
            }
        }
    }.flow

    override fun upcomingStudents(
        withinDays: Int,
        pageSize: Int
    ) = Pager(
        config =PagingConfig(pageSize=pageSize)
    ){
        val todayDate = LocalDate.now()
        val today = todayDate.dayOfMonth
        val tillDay = todayDate.lengthOfMonth().let { lastDayOfMonth ->
            val day = today + withinDays
            if (day > lastDayOfMonth) day - lastDayOfMonth else day
        }

        studentDao.upcomingStudents(today,tillDay)
    }.flow

    override fun pendingStudents(pageSize:Int) = Pager(
        config =PagingConfig(pageSize=pageSize)
    ){
        studentDao.pendingStudents()
    }.flow


    override suspend fun pendingFeeMonthHistoryOfStudent(sid: Long):List<FeeHistory> = withContext(Dispatchers.IO){
        coroutineScope {
            val lastPaidDate = transactionDao.lastPaidDateForStudent(sid)
            listOf(
                async{
                feeHistoryDao.currentFeeHistory(sid,lastPaidDate)
                }.await()
            ) +
                    async{
                        feeHistoryDao.feeHistoryAfterLastPaidDate(sid, lastPaidDate )
                    }.await()
        }
    }




    override suspend fun student(sid:Long)= withContext(Dispatchers.IO){
        studentDao.student(sid)
    }

    override  fun transactionsForStudent(sid: Long,pageSize: Int) = Pager(
        config = PagingConfig( pageSize = pageSize )){
            transactionDao.forStudent(sid)
        }.flow

    override suspend fun insertStudent(student: Student) {
        val request =OneTimeWorkRequestBuilder<StudentWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(student.toWorkData(Operation.INSERT))
            .build()
        workManager.enqueue(request)
    }

    override suspend fun updateStudent(student: Student) {
        val request = OneTimeWorkRequestBuilder<StudentWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("${student.id}")
            .setInputData(student.toWorkData(Operation.UPDATE)).build()

        workManager.enqueueUniqueWork( "${student.id}",
            ExistingWorkPolicy.KEEP,
             request
        )
    }

    override suspend fun deleteStudent(student: Student) {
        val request = OneTimeWorkRequestBuilder<StudentWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("${student.id}")
            .setInputData(student.toWorkData(Operation.DELETE)).build()

        workManager.enqueueUniqueWork( "${student.id}",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        val request = OneTimeWorkRequestBuilder<TransactionWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("${transaction.sid}")
            .setInputData(transaction.toWorkData(Operation.INSERT)).build()
        workManager.enqueueUniqueWork("${transaction.sid}${transaction.paidTillDate}",
            ExistingWorkPolicy.KEEP,
            request
        )
    }



    override suspend fun deleteTransaction(transaction: Transaction) {
        val request = OneTimeWorkRequestBuilder<TransactionWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("${transaction.sid}")
            .setInputData(transaction.toWorkData(Operation.DELETE)).build()
        workManager.enqueueUniqueWork("${transaction.sid}${transaction.paidTillDate}",
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    override suspend fun insertFeeHistory(feeHistory: FeeHistory) {
        val request = OneTimeWorkRequestBuilder<FeeHistoryWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("${feeHistory.sid}")
            .setInputData(feeHistory.toWorkData(Operation.INSERT)).build()
        workManager.enqueueUniqueWork("${feeHistory.sid}",
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    override suspend fun updateFeeHistory(feeHistory: FeeHistory) {
        val request = OneTimeWorkRequestBuilder<TransactionWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("${feeHistory.sid}")
            .setInputData(feeHistory.toWorkData(Operation.UPDATE)).build()
        workManager.enqueueUniqueWork("${feeHistory.sid}",
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    override suspend fun deleteFeeHistory(feeHistory: FeeHistory) {
        val request = OneTimeWorkRequestBuilder<TransactionWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag("${feeHistory.sid}")
            .setInputData(feeHistory.toWorkData(Operation.DELETE)).build()
        workManager.enqueueUniqueWork("${feeHistory.sid}",
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    override fun anyPendingTransaction(id: Long): Flow<Boolean> {
        val workQuery = WorkQuery.Builder
            .fromTags(listOf("$id"))
            .addStates(listOf(WorkInfo.State.ENQUEUED,WorkInfo.State.RUNNING))
            .build()

         return workManager.getWorkInfosFlow(workQuery).map { it.isNotEmpty() }

    }



}

