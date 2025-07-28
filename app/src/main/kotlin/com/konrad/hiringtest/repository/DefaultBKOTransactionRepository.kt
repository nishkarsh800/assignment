package com.konrad.hiringtest.repository

import com.konrad.hiringtest.model.Result
import com.konrad.hiringtest.model.network.toTransactionList
import com.konrad.hiringtest.model.ui.Transaction
import com.konrad.hiringtest.network.BKOService
import timber.log.Timber

class DefaultBKOTransactionRepository(
    private val bkoService: BKOService
) : TransactionRepository {

    override suspend fun getAllTransactions(): Result<List<Transaction>> {
        return try {
            val bkoResponse = bkoService.getAllTransactions()
            val bkoResponseBody = bkoResponse.body()

            if (bkoResponse.isSuccessful && bkoResponseBody != null) {
                Result.Success(bkoResponseBody.toTransactionList())
            } else {
                val throwable = Throwable("There was an issue fetching BKO transactions: ${bkoResponse.errorBody().toString()}")
                Timber.e(throwable)
                Result.Error(throwable)
            }

        } catch (exception: Exception) {
            Timber.e(exception)
            Result.Error(exception)
        }
    }
}