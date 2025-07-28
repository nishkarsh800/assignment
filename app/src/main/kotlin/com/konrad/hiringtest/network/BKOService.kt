package com.konrad.hiringtest.network

import com.konrad.hiringtest.model.network.BKOGetAllTransactionsResponse
import retrofit2.Response
import retrofit2.http.GET

interface BKOService {

    @GET("BKO.json")
    suspend fun getAllTransactions(): Response<BKOGetAllTransactionsResponse>
}