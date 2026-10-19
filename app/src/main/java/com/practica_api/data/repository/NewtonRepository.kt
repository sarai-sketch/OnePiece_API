package com.practica_api.data.repository

import com.practica_api.core.safeApiCall
import com.practica_api.data.remote.mapper.toDomain
import com.practica_api.data.remote.service.NewtonService
import com.practica_api.domain.model.NewtonResult

class NewtonRepository(private val service: NewtonService) {
    suspend fun calculate(operation: String, expression: String): Result<NewtonResult> =
        safeApiCall { service.calculate(operation, expression).toDomain() }
}
