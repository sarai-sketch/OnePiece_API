package com.practica_api.data.repository

import com.practica_api.core.safeApiCall
import com.practica_api.data.remote.mapper.toDomain
import com.practica_api.data.remote.service.OnePieceService
import com.practica_api.domain.model.DevilFruit
import com.practica_api.domain.model.OnePieceCharacter
import com.practica_api.domain.model.Saga

/**
 * Repositorio de la API de One Piece.
 * Consume los personajes, frutas y sagas exclusivamente de la API pública con Retrofit.
 */
class OnePieceRepository(
    private val service: OnePieceService
) {

    @Volatile private var characters: List<OnePieceCharacter>? = null
    @Volatile private var fruits: List<DevilFruit>? = null
    @Volatile private var sagas: List<Saga>? = null

    suspend fun getCharacters(forceRefresh: Boolean = false): Result<List<OnePieceCharacter>> {
        if (!forceRefresh && !characters.isNullOrEmpty()) {
            return Result.success(characters!!)
        }
        return safeApiCall {
            service.getCharacters().map { it.toDomain() }
        }.onSuccess { characters = it }
    }

    suspend fun getFruits(forceRefresh: Boolean = false): Result<List<DevilFruit>> {
        if (!forceRefresh && !fruits.isNullOrEmpty()) {
            return Result.success(fruits!!)
        }
        return safeApiCall {
            service.getFruits().map { it.toDomain() }
        }.onSuccess { fruits = it }
    }

    suspend fun getSagas(forceRefresh: Boolean = false): Result<List<Saga>> {
        if (!forceRefresh && !sagas.isNullOrEmpty()) {
            return Result.success(sagas!!)
        }
        return safeApiCall {
            service.getSagas().map { it.toDomain() }
        }.onSuccess { sagas = it }
    }
}
