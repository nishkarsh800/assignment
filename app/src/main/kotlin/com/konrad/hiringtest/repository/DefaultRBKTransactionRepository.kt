package com.konrad.hiringtest.repository

import com.konrad.hiringtest.model.Result
import com.konrad.hiringtest.model.network.toTransactionList
import com.konrad.hiringtest.model.ui.Transaction
import com.konrad.hiringtest.network.RBKService
import timber.log.Timber

class DefaultRBKTransactionRepository(
    private val rbkService: RBKService
) : TransactionRepository {

    override suspend fun getAllTransactions(): Result<List<Transaction>> {
        return try {
            val rbkResponse = rbkService.getAllTransactions()
            val rbkResponseBody = rbkResponse.body()

            if (rbkResponse.isSuccessful && rbkResponseBody != null) {
                Result.Success(rbkResponseBody.toTransactionList())
            } else {
                val throwable = Throwable("There was an issue fetching RBK transactions: ${rbkResponse.errorBody().toString()}")
                Timber.e(throwable)
                Result.Error(throwable)
            }

        } catch (exception: Exception) {
            Timber.e(exception)
            Result.Error(exception)
        }
    }
}