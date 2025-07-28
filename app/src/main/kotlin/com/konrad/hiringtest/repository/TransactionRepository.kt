package com.konrad.hiringtest.repository

import com.konrad.hiringtest.model.Result
import com.konrad.hiringtest.model.ui.Transaction

interface TransactionRepository {
    suspend fun getAllTransactions(): Result<List<Transaction>>
}