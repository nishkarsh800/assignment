package com.konrad.hiringtest.network

import com.konrad.hiringtest.model.network.RBKGetAllTransactionsResponse
import retrofit2.Response
import retrofit2.http.GET

interface RBKService {

    @GET("RBK.json")
    suspend fun getAllTransactions(): Response<RBKGetAllTransactionsResponse>
}