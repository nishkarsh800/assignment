package com.konrad.hiringtest.model.network

import com.konrad.hiringtest.model.ui.BankType
import com.konrad.hiringtest.model.ui.Transaction
import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class RBKGetAllTransactionsResponse(
    val data: List<RBKTransaction>
) {
    @JsonClass(generateAdapter = true)
    data class RBKTransaction(
        val transactionId: String,
        val description: String,
        val transactionAmount: BigDecimal,
        val currency: Currency,
        val isWithdrawal: Boolean,
        val date: LocalDate,
        val accountId: String
    ) {
        enum class Currency {
            CAD, USD
        }
    }
}

fun RBKGetAllTransactionsResponse.toTransactionList(): List<Transaction> {
    return data.map { rbkTransaction ->
        Transaction(
            rbkTransaction.date,
            if (rbkTransaction.isWithdrawal) rbkTransaction.transactionAmount else rbkTransaction.transactionAmount.negate(),
            rbkTransaction.description,
            emptyList(),
            BankType.RBK
        )
    }
}