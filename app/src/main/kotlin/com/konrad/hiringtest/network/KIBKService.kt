package com.konrad.hiringtest.network

import com.konrad.hiringtest.model.network.KIBKGetAllTransactionsResponse
import retrofit2.Response
import retrofit2.http.GET

interface KIBKService {

    @GET("KIBK.json")
    suspend fun getAllTransactions(): Response<KIBKGetAllTransactionsResponse>
}