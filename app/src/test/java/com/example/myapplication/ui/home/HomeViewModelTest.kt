@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.myapplication.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.paging.testing.asSnapshot
import com.example.myapplication.data.room.dao.NameWithFeeDate
import com.example.myapplication.model.NameWithPendingMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.time.LocalDate
import java.time.Period

class HomeViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private val feeDate= LocalDate.now().minusMonths(2)


    private lateinit var  homeViewModel :HomeViewModel

    private val student1= NameWithFeeDate(id=1,firstName="AA", lastName ="",classYear = 1, pendingMonths = 1, feeDate = feeDate)
    private val studentsSortedByName = listOf(student1,
        student1.copy(firstName = "AB"),
        student1.copy(firstName = "BA"),
        student1.copy(firstName = "BB"))
    private val expectedPendingMonth = pending_month_calculator(feeDate)+student1.pendingMonths
    private val expectedStudentSortedByName =NameWithPendingMonth.StudentItem(id=1,name="AA",classYear=1, pendingMonths = expectedPendingMonth)

    private val expectedStudentsSortedByName = listOf(
        NameWithPendingMonth.SeparatorItem("A"),
        expectedStudentSortedByName,
        expectedStudentSortedByName.copy(name="AB"),
        NameWithPendingMonth.SeparatorItem("B"),
        expectedStudentSortedByName.copy(name="BA"),
        expectedStudentSortedByName.copy(name="BB"))


    @Before
    fun setUp(){
        homeViewModel = HomeViewModel(savedStateHandle = SavedStateHandle(),
            studentRepository=FakeStudentRepository1(studentsSortedByName))
    }
    @Test
    fun check_view_model_transformations() = runTest{

        val students = homeViewModel.students
        val studentItems = students.asSnapshot()

        assertThat(studentItems).containsExactlyElementsOf(expectedStudentsSortedByName)
    }

    private fun pending_month_calculator(lastPaidDate:LocalDate) =
        Period.between(lastPaidDate, LocalDate.now()).months


    @Test
    fun test_pending_month_calculator(){
        val lastPaidDate = LocalDate.now().minusMonths(2)
        val pendingMonths = pending_month_calculator(lastPaidDate)
        assertThat(pendingMonths).isEqualTo(2)

    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
}





class MainDispatcherRule  (
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}