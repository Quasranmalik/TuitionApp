package com.example.myapplication.workers

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.myapplication.data.room.dao.StudentDao
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.worker.StudentWorker
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(application=HiltTestApplication::class)
class WorkersTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var studentDao: StudentDao
    lateinit var workManager:WorkManager

    val student = Student(firstName="Quasran", lastName = "A", pendingMonths = 1, classYear=10)


    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context,config)
        workManager = WorkManager.getInstance(context)
        hiltRule.inject()
    }

    @Test
    fun insert_student_worker(){
        val request = OneTimeWorkRequestBuilder<StudentWorker>()
            .build()
        workManager.enqueue(request).result.get()
        val workInfo = workManager.getWorkInfoById(request.id).get()
        assertThat(workInfo.state).isEqualTo(WorkInfo.State.SUCCEEDED)
//        assertThat(runBlocking{studentDao.student(0)}).isEqualTo(student)
    }

    @Test
    fun first_name_null_gives_error(){

    }

}

