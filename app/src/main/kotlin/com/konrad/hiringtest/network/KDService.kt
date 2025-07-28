package com.konrad.hiringtest.network

import com.konrad.hiringtest.model.network.KDGetAllTransactionsResponse
import retrofit2.Response
import retrofit2.http.GET

interface KDService {

    @GET("KD.json")
    suspend fun getAllTransactions(): Response<KDGetAllTransactionsResponse>
}