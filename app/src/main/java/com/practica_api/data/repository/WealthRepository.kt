package com.practica_api.data.repository

import com.practica_api.core.safeApiCall
import com.practica_api.data.remote.mapper.toDomain
import com.practica_api.data.remote.service.WealthService
import com.practica_api.domain.model.WealthComparison

class WealthRepository(private val service: WealthService) {
    suspend fun compare(
        wealthYours: String,
        wealthTheirs: String,
        spend: String,
    ): Result<WealthComparison> = safeApiCall {
        service.compare(
            expression = "durationOfDailySpend",
            wealthYours = wealthYours,
            wealthTheirs = wealthTheirs,
            spend = spend,
        ).toDomain()
    }
}
