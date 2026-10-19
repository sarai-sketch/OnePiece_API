package com.practica_api.data.remote.client

import com.practica_api.data.remote.service.NewtonService
import com.practica_api.data.remote.service.OnePieceService
import com.practica_api.data.remote.service.WealthService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Único punto donde se configura la red: un OkHttpClient compartido
 * y una instancia de Retrofit por cada API (cada una con su base URL).
 */
object RetrofitClient {

    private const val ONE_PIECE_URL = "https://api.api-onepiece.com/v2/"
    private const val NEWTON_URL = "https://newton.vercel.app/api/v2/"
    private const val WEALTH_URL = "https://api.wegtultrarich.org/v1/"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
    }

    private fun retrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val onePieceService: OnePieceService by lazy { retrofit(ONE_PIECE_URL).create(OnePieceService::class.java) }
    val newtonService: NewtonService by lazy { retrofit(NEWTON_URL).create(NewtonService::class.java) }
    val wealthService: WealthService by lazy { retrofit(WEALTH_URL).create(WealthService::class.java) }
}
