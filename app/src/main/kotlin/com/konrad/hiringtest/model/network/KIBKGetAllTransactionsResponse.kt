package com.konrad.hiringtest.model.network

import com.konrad.hiringtest.model.ui.BankType
import com.konrad.hiringtest.model.ui.Transaction
import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class KIBKGetAllTransactionsResponse(
    val data: List<KIBKTransaction>
) {
    @JsonClass(generateAdapter = true)
    data class KIBKTransaction(
        val date: LocalDate,
        val amount: BigDecimal,
        val transactionId: String,
        val details: String,
        val category: List<String>,
        val accountNumber: String
    )
}

fun KIBKGetAllTransactionsResponse.toTransactionList(): List<Transaction> {
    return data.map { kibkTransaction ->
        Transaction(
            kibkTransaction.date,
            kibkTransaction.amount,
            kibkTransaction.details,
            kibkTransaction.category,
            BankType.KIBK
        )
    }
}