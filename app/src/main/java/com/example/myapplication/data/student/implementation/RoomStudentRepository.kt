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
import com.example.myapplication.data.room.dao.FeeHistoryDao
import com.example.myapplication.data.room.dao.NameWithFeeDate
import com.example.myapplication.data.room.dao.StudentDao
import com.example.myapplication.data.room.dao.TransactionDao
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction
import com.example.myapplication.data.student.StudentRepository
import com.example.myapplication.ui.home.model.SortField
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
import org.jetbrains.annotations.VisibleForTesting
import java.time.LocalDate
import java.time.Period
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


    private suspend fun pendingFeeMonthHistoryOfStudent(studentId: Long, lastPaidDate:LocalDate?=null):List<FeeHistory> = withContext(Dispatchers.IO){
        coroutineScope {
            val _lastPaidDate = lastPaidDate ?:transactionDao.lastPaidDateForStudent(studentId)
            listOf(
                async{
                feeHistoryDao.currentFeeHistory(studentId,_lastPaidDate)
                }.await()
            ) +
                    async{
                        feeHistoryDao.feeHistoryAfterLastPaidDate(studentId, _lastPaidDate )
                    }.await()
        }
    }

    override suspend fun getPendingAmount(studentId: Long): Int = withContext(Dispatchers.IO){
        val lastPaidDate = transactionDao.lastPaidDateForStudent(studentId)


        getPendingAmount(pendingFeeMonthHistoryOfStudent(studentId,lastPaidDate),lastPaidDate)
    }



    override suspend fun student(studentId:Long)= withContext(Dispatchers.IO){
        studentDao.student(studentId)
    }

    override  fun transactionsForStudent(studentId: Long, pageSize: Int) = Pager(
        config = PagingConfig( pageSize = pageSize )){
            transactionDao.forStudent(studentId)
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

    override fun anyPendingTransaction(studentId: Long): Flow<Boolean> {
        val workQuery = WorkQuery.Builder
            .fromTags(listOf("$studentId"))
            .addStates(listOf(WorkInfo.State.ENQUEUED,WorkInfo.State.RUNNING))
            .build()

         return workManager.getWorkInfosFlow(workQuery).map { it.isNotEmpty() }

    }



}
@VisibleForTesting
fun getPendingAmount(pendingFeeHistory: List<FeeHistory>, lastPaidDate: LocalDate?,
                             today:LocalDate = LocalDate.now()):Int{
    if (pendingFeeHistory.isEmpty()) return 0

    var fee:Int;var startFeeDate:LocalDate;var endFeeDate:LocalDate
    var pendingFee = 0
    for(index in 0 until pendingFeeHistory.count() ) {
        with(pendingFeeHistory[index]){
            fee=this.fee
            startFeeDate = if (index == 0 ) lastPaidDate?:this.joinDate else this.joinDate
        }
        endFeeDate = if (index == pendingFeeHistory.count()-1) today else pendingFeeHistory[index +1].joinDate

        pendingFee += Period.between(startFeeDate,endFeeDate).months * fee
    }

    return pendingFee
}