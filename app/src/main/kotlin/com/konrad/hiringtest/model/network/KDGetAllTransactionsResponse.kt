package com.konrad.hiringtest.model.network

import com.konrad.hiringtest.model.ui.BankType
import com.konrad.hiringtest.model.ui.Transaction
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class KDGetAllTransactionsResponse(
    val data: List<KDTransaction>
) {
    @JsonClass(generateAdapter = true)
    data class KDTransaction(
        val id: String,
        val transactionTime: LocalDate,
        val amount: Amount,
        val from: Account,
        val to: Account,
        val type: TransactionType,
        val tags: List<String>?
    ) {
        @JsonClass(generateAdapter = true)
        data class Amount(
            val amount: BigDecimal,
            val currency: String
        )

        @JsonClass(generateAdapter = true)
        data class Account(
            val id: String,
            val name: String
        )

        enum class TransactionType {
            @Json(name = "withdrawal")
            WITHDRAWAL,

            @Json(name = "deposit")
            DEPOSIT
        }
    }
}

fun KDGetAllTransactionsResponse.toTransactionList(): List<Transaction> {
    return data.map { kdTransaction ->
        Transaction(
            kdTransaction.transactionTime,
            if (kdTransaction.type == KDGetAllTransactionsResponse.KDTransaction.TransactionType.WITHDRAWAL) kdTransaction.amount.amount else kdTransaction.amount.amount.negate(),
            if (kdTransaction.type == KDGetAllTransactionsResponse.KDTransaction.TransactionType.WITHDRAWAL) kdTransaction.to.name else kdTransaction.from.name,
            kdTransaction.tags ?: emptyList(),
            BankType.KD
        )
    }
}