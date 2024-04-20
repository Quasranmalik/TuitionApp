package com.example.myapplication.data.student.implementation

import com.example.myapplication.data.room.model.FeeHistory
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class RoomStudentRepositoryTest {



    private val pendingFeeHistory = listOf(
        FeeHistory(sid=1,joinDate = LocalDate.of(2020,1,1),fee=100),
        FeeHistory(sid=1, joinDate = LocalDate.of(2020,3,1),fee=200),
        FeeHistory(sid=1, joinDate = LocalDate.of(2020,4,1),fee=300),
        FeeHistory(sid=1, joinDate = LocalDate.of(2020,5,1),fee=400)
    )

    val lastPaidDate = LocalDate.of(2020,2,1)

    private val today = LocalDate.of(2020,6,1)
    @Test
    fun checkPendingAmount() {
        assertThat(getPendingAmount(pendingFeeHistory,lastPaidDate,today)).isEqualTo(1000)
        assertThat(getPendingAmount(pendingFeeHistory,null,today)).isEqualTo(1100)
    }


}