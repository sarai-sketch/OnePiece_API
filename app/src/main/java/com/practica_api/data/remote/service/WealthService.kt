package com.practica_api.data.remote.service

import com.practica_api.data.remote.dto.WealthResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WealthService {
    @GET("comparison")
    suspend fun compare(
        @Query("expression") expression: String,
        @Query("wealthYours") wealthYours: String,
        @Query("wealthTheirs") wealthTheirs: String,
        @Query("spend") spend: String,
    ): WealthResponseDto
}
