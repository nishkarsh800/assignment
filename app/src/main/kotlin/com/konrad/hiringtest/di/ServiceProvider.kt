package com.konrad.hiringtest.di

import com.konrad.hiringtest.network.BKOService
import com.konrad.hiringtest.network.KDService
import com.konrad.hiringtest.network.KIBKService
import com.konrad.hiringtest.network.RBKService
import com.konrad.hiringtest.util.BigDecimalAdapter
import com.konrad.hiringtest.util.LocalDateAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigDecimal
import java.time.LocalDate

/**
 * A simple implementation of the service provider pattern. This is an alternative to Dagger.
 */
object ServiceProvider {

    private val retrofit: Retrofit by lazy {
        val moshi = Moshi.Builder()
            .add(LocalDate::class.java, LocalDateAdapter())
            .add(BigDecimal::class.java, BigDecimalAdapter())
            .build()

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://kghiretest.s3.amazonaws.com/")
            .client(httpClient)
            .build()
    }

    val bkoService: BKOService by lazy {
        retrofit.create(BKOService::class.java)
    }

    val kibkService: KIBKService by lazy {
        retrofit.create(KIBKService::class.java)
    }

    val rbkService: RBKService by lazy {
        retrofit.create(RBKService::class.java)
    }

    val kdService: KDService by lazy {
        retrofit.create(KDService::class.java)
    }
}