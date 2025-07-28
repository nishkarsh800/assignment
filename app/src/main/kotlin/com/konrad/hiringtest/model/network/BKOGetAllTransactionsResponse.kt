package com.konrad.hiringtest.model.network

import com.konrad.hiringtest.model.ui.BankType
import com.konrad.hiringtest.model.ui.Transaction
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class BKOGetAllTransactionsResponse(
    val data: List<BKOTransaction>
) {
    @JsonClass(generateAdapter = true)
    data class BKOTransaction(
        val date: LocalDate,
        val amount: BigDecimal,
        val contact: Contact,
        val account: Account,
        val isDeposit: Boolean,
        val note: String?
    ) {
        @JsonClass(generateAdapter = true)
        data class Contact(
            val contactId: String,
            val name: String
        )

        @JsonClass(generateAdapter = true)
        data class Account(
            val accountId: String,
            val name: String,
            val type: AccountType
        ) {
            enum class AccountType {
                @Json(name = "chequing")
                CHEQUING,

                @Json(name = "savings")
                SAVINGS,

                @Json(name = "credit")
                CREDIT
            }
        }
    }
}

fun BKOGetAllTransactionsResponse.toTransactionList(): List<Transaction> {
    return data.map { bkoTransaction ->
        Transaction(
            bkoTransaction.date,
            bkoTransaction.amount,
            bkoTransaction.contact.name,
            emptyList(),
            BankType.BKO
        )
    }
}