package com.example.myapplication.worker

import com.example.myapplication.Operation
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException

import org.junit.Test
import java.time.LocalDate

class UtilsKtTest {

    private val student = Student(id=0,firstName ="Quasran",classYear=10)
    private val transaction = Transaction(sid=0,paidTillDate= LocalDate.now(),month=3)
    private val feeHistory = FeeHistory(sid=0, joinDate = LocalDate.now(),fee=0)
    @Test
    fun toStudent() {
        assertThat(student.toWorkData(Operation.INSERT).toStudent()).isEqualTo(student)
    }

    @Test
    fun verify_that_student_negative_id_throws_exception(){
        assertThatIllegalArgumentException()
            .isThrownBy { student.copy(id=-1).toWorkData(Operation.INSERT).toStudent() }
            .withMessageContaining("ID")
    }

    @Test
    fun verify_that_student_empty_first_name_throws_exception(){
        assertThatIllegalArgumentException().isThrownBy { student.copy(firstName="").toWorkData(Operation.INSERT).toStudent() }
            .withMessageContaining("Empty")
    }

    @Test
    fun verify_that_student_negative_class_year_throws_exception(){
        assertThatIllegalArgumentException().isThrownBy { student.copy(classYear = -2).toWorkData(Operation.INSERT).toStudent() }
            .withMessageContainingAll("Class","Negative")
    }


    @Test
    fun check_to_transaction(){
        assertThat(transaction.toWorkData(Operation.INSERT).toTransaction()).isEqualTo(transaction)
    }

    @Test
    fun verify_that_transaction_negative_id_throws_exception(){
        assertThatIllegalArgumentException().isThrownBy { transaction.copy(sid=-1).toWorkData(Operation.INSERT)
            .toTransaction()}.withMessageContainingAll("Student","ID")
    }

    @Test
    fun verify_that_transaction_zero_or_negative_month_throws_exception(){
        listOf(0,-5).forEach{month ->
            assertThatIllegalArgumentException().isThrownBy { transaction.copy(month=month)
                .toWorkData(Operation.INSERT).toTransaction()}
                .withMessageContaining("Month")
        }

    }
    @Test
    fun check_to_fee_History(){
        assertThat(feeHistory.toWorkData(Operation.INSERT).toFeeHistory())
            .isEqualTo(feeHistory)
    }

    @Test
    fun verify_that_fee_history_negative_id_throws_exception(){
        assertThatIllegalArgumentException().isThrownBy { feeHistory.copy(sid=-1).toWorkData(Operation.INSERT)
            .toTransaction()}.withMessageContainingAll("Student","ID")
    }

    @Test
    fun verify_that_fee_history_negative_fee_throws_exception(){
        assertThatIllegalArgumentException().isThrownBy { feeHistory.copy(fee=-100).toWorkData(Operation.INSERT)
            .toFeeHistory()}.withMessageContaining("Fee")
    }



}