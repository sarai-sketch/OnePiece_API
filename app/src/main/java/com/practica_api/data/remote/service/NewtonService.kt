package com.practica_api.data.remote.service

import com.practica_api.data.remote.dto.NewtonDto
import retrofit2.http.GET
import retrofit2.http.Path

interface NewtonService {
    /** operation: derive, integrate, simplify, factor... Retrofit codifica la expresión (x^2 -> x%5E2). */
    @GET("{operation}/{expression}")
    suspend fun calculate(
        @Path("operation") operation: String,
        @Path("expression") expression: String,
    ): NewtonDto
}
