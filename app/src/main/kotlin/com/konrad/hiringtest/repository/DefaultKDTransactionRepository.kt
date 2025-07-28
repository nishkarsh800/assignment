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
        return try {
            val kdResponse = kdService.getAllTransactions()
            val kdResponseBody = kdResponse.body()

            if (kdResponse.isSuccessful && kdResponseBody != null) {
                Result.Success(kdResponseBody.toTransactionList())
            } else {
                val throwable = Throwable("There was an issue fetching KD transactions: ${kdResponse.errorBody().toString()}")
                Timber.e(throwable)
                Result.Error(throwable)
            }

        } catch (exception: Exception) {
            Timber.e(exception)
            Result.Error(exception)
        }
    }
}