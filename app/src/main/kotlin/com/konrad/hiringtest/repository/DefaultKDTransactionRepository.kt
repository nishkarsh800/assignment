package com.konrad.hiringtest.repository

import com.konrad.hiringtest.model.Result
import com.konrad.hiringtest.model.network.toTransactionList
import com.konrad.hiringtest.model.ui.Transaction
import com.konrad.hiringtest.network.KDService
import timber.log.Timber

class DefaultKDTransactionRepository(
    private val kdService: KDService
) : TransactionRepository {

    override suspend fun getAllTransactions(): Result<List<Transaction>> {
        return Result.Success(emptyList())
    }
}