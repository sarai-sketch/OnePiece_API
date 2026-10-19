package com.practica_api.data.repository

import com.practica_api.core.safeApiCall
import com.practica_api.data.remote.mapper.toDomain
import com.practica_api.data.remote.service.OnePieceService
import com.practica_api.domain.model.DevilFruit
import com.practica_api.domain.model.OnePieceCharacter
import com.practica_api.domain.model.Saga

/** Pide DTOs al servicio, los convierte a modelos de dominio y los guarda en caché en memoria. */
class OnePieceRepository(private val service: OnePieceService) {

    @Volatile private var characters: List<OnePieceCharacter>? = null
    @Volatile private var fruits: List<DevilFruit>? = null
    @Volatile private var sagas: List<Saga>? = null

    suspend fun getCharacters(forceRefresh: Boolean = false): Result<List<OnePieceCharacter>> =
        safeApiCall {
            characters.takeUnless { forceRefresh }
                ?: service.getCharacters().map { it.toDomain() }.also { characters = it }
        }

    suspend fun getFruits(forceRefresh: Boolean = false): Result<List<DevilFruit>> =
        safeApiCall {
            fruits.takeUnless { forceRefresh }
                ?: service.getFruits().map { it.toDomain() }.also { fruits = it }
        }

    suspend fun getSagas(forceRefresh: Boolean = false): Result<List<Saga>> =
        safeApiCall {
            sagas.takeUnless { forceRefresh }
                ?: service.getSagas().map { it.toDomain() }.also { sagas = it }
        }
}
