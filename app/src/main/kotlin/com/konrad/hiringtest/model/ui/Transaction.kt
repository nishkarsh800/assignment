package com.konrad.hiringtest.model.ui

import java.math.BigDecimal
import java.time.LocalDate

data class Transaction(
    val date: LocalDate,
    val value: BigDecimal,
    val description: String,
    val tags: List<String>,
    val source: BankType
)