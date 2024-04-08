package com.example.myapplication.worker

import androidx.work.Data
import androidx.work.workDataOf
import com.example.myapplication.KEY_CLASS
import com.example.myapplication.KEY_DISCOUNT
import com.example.myapplication.KEY_FEE
import com.example.myapplication.KEY_FIRST_NAME
import com.example.myapplication.KEY_ID
import com.example.myapplication.KEY_JOIN_DATE
import com.example.myapplication.KEY_LAST_NAME
import com.example.myapplication.KEY_MONTH
import com.example.myapplication.KEY_OPERATION_TYPE
import com.example.myapplication.KEY_PAID_TILL_DATE
import com.example.myapplication.KEY_PENDING_MONTHS
import com.example.myapplication.Operation
import com.example.myapplication.data.room.model.FeeHistory
import com.example.myapplication.data.room.model.Student
import com.example.myapplication.data.room.model.Transaction
import java.time.LocalDate
import kotlin.IllegalArgumentException

fun Student.toWorkData(operation: Operation) = workDataOf(KEY_OPERATION_TYPE to operation.ordinal,
                                            KEY_ID to id,
                                            KEY_FIRST_NAME to firstName,
                                            KEY_LAST_NAME to lastName,
                                            KEY_CLASS to classYear,
                                            KEY_PENDING_MONTHS to pendingMonths
                                            )


fun Data.toStudent(): Student =

    Student(id = getLong(KEY_ID,-1)
        .also { if (it<0) throw IllegalArgumentException("Student ID is negative") },
        firstName = getString(KEY_FIRST_NAME)
            ?.also{
                if (it.isBlank()) throw  IllegalArgumentException("First Name is Empty")
            }
            ?:throw IllegalArgumentException("First Name is null"),
        lastName = getString(KEY_LAST_NAME),
        classYear = getInt(KEY_CLASS,-1)
            .also{if (it < 0) throw IllegalArgumentException("Class Year is Negative")},
        pendingMonths = getInt(KEY_PENDING_MONTHS,-1)
            .also{if (it < 0) throw IllegalArgumentException("PendingMonths Is Negative")}
    )


fun Transaction.toWorkData(operation: Operation) = workDataOf(KEY_OPERATION_TYPE to operation.ordinal,
                                        KEY_ID to sid,
                                        KEY_PAID_TILL_DATE to paidTillDate.toEpochDay(),
                                        KEY_MONTH to month,
                                        KEY_DISCOUNT to discount)

fun Data.toTransaction() =
        Transaction(sid = getLong(KEY_ID,-1)
            .also { if (it<0) throw IllegalArgumentException("Student ID is negative") },
            paidTillDate = getLong(KEY_PAID_TILL_DATE ,0).let{epochDay ->
                          if (epochDay <= 0) throw IllegalArgumentException("Local Date is null")
                            LocalDate.ofEpochDay(epochDay)
            },
            month =getInt(KEY_MONTH,0)
                .also{if (it <=0) throw IllegalArgumentException("Month is Invalid")},
            discount = getInt(KEY_DISCOUNT ,-1)
        )

fun FeeHistory.toWorkData(operation: Operation) =
    workDataOf(KEY_OPERATION_TYPE to operation.ordinal,
            KEY_ID to sid,
            KEY_JOIN_DATE to joinDate.toEpochDay(),
            KEY_FEE to fee)

fun Data.toFeeHistory()=
    FeeHistory(sid=getLong(KEY_ID,-1)
                    .also { if (it<0) throw IllegalArgumentException("Student ID is negative")},
                joinDate=getLong(KEY_JOIN_DATE ,0).let{ epochDay ->
                    if (epochDay <= 0) throw IllegalArgumentException("Local Date is null")
                    LocalDate.ofEpochDay(epochDay)
                },
                fee=getInt(KEY_FEE,-1)
                    .also{if (it<0) throw IllegalArgumentException("Negative Fee Value")})

