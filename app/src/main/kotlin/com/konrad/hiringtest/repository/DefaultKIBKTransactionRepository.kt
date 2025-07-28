package com.konrad.hiringtest.repository

import com.konrad.hiringtest.model.Result
import com.konrad.hiringtest.model.network.toTransactionList
import com.konrad.hiringtest.model.ui.Transaction
import com.konrad.hiringtest.network.KIBKService
import timber.log.Timber

class DefaultKIBKTransactionRepository(
    private val kibkService: KIBKService
) : TransactionRepository {

    override suspend fun getAllTransactions(): Result<List<Transaction>> {
        return try {
            val kibkResponse = kibkService.getAllTransactions()
            val kibkResponseBody = kibkResponse.body()

            if (kibkResponse.isSuccessful && kibkResponseBody != null) {
                Result.Success(kibkResponseBody.toTransactionList())
            } else {
                val throwable = Throwable("There was an issue fetching KIBK transactions: ${kibkResponse.errorBody().toString()}")
                Timber.e(throwable)
                Result.Error(throwable)
            }

        } catch (exception: Exception) {
            Timber.e(exception)
            Result.Error(exception)
        }
    }
}