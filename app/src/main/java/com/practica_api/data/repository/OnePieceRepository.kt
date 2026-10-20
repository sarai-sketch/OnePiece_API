package com.practica_api.data.repository

import com.practica_api.core.safeApiCall
import com.practica_api.data.local.CharacterCacheDao
import com.practica_api.data.local.FruitCacheDao
import com.practica_api.data.local.SagaCacheDao
import com.practica_api.data.remote.mapper.toCacheEntity
import com.practica_api.data.remote.mapper.toDomain
import com.practica_api.data.remote.service.OnePieceService
import com.practica_api.domain.model.DevilFruit
import com.practica_api.domain.model.OnePieceCharacter
import com.practica_api.domain.model.Saga

/**
 * Repositorio Offline-First:
 * Intenta obtener datos de la API de One Piece. Si tiene éxito, los guarda en la base de datos Room.
 * Si no hay conexión a internet, recupera automáticamente los datos guardados en la caché local.
 */
class OnePieceRepository(
    private val service: OnePieceService,
    private val characterCacheDao: CharacterCacheDao? = null,
    private val fruitCacheDao: FruitCacheDao? = null,
    private val sagaCacheDao: SagaCacheDao? = null,
) {

    @Volatile private var characters: List<OnePieceCharacter>? = null
    @Volatile private var fruits: List<DevilFruit>? = null
    @Volatile private var sagas: List<Saga>? = null

    suspend fun getCharacters(forceRefresh: Boolean = false): Result<List<OnePieceCharacter>> {
        if (!forceRefresh && !characters.isNullOrEmpty()) {
            return Result.success(characters!!)
        }
        val netResult = safeApiCall {
            service.getCharacters().map { it.toDomain() }
        }
        return if (netResult.isSuccess) {
            val list = netResult.getOrNull().orEmpty()
            characters = list
            characterCacheDao?.insertarTodos(list.map { it.toCacheEntity() })
            netResult
        } else {
            val cached = characterCacheDao?.obtenerTodos()?.map { it.toDomain() }.orEmpty()
            if (cached.isNotEmpty()) {
                characters = cached
                Result.success(cached)
            } else {
                netResult
            }
        }
    }

    suspend fun getFruits(forceRefresh: Boolean = false): Result<List<DevilFruit>> {
        if (!forceRefresh && !fruits.isNullOrEmpty()) {
            return Result.success(fruits!!)
        }
        val netResult = safeApiCall {
            service.getFruits().map { it.toDomain() }
        }
        return if (netResult.isSuccess) {
            val list = netResult.getOrNull().orEmpty()
            fruits = list
            fruitCacheDao?.insertarTodos(list.map { it.toCacheEntity() })
            netResult
        } else {
            val cached = fruitCacheDao?.obtenerTodos()?.map { it.toDomain() }.orEmpty()
            if (cached.isNotEmpty()) {
                fruits = cached
                Result.success(cached)
            } else {
                netResult
            }
        }
    }

    suspend fun getSagas(forceRefresh: Boolean = false): Result<List<Saga>> {
        if (!forceRefresh && !sagas.isNullOrEmpty()) {
            return Result.success(sagas!!)
        }
        val netResult = safeApiCall {
            service.getSagas().map { it.toDomain() }
        }
        return if (netResult.isSuccess) {
            val list = netResult.getOrNull().orEmpty()
            sagas = list
            sagaCacheDao?.insertarTodos(list.map { it.toCacheEntity() })
            netResult
        } else {
            val cached = sagaCacheDao?.obtenerTodos()?.map { it.toDomain() }.orEmpty()
            if (cached.isNotEmpty()) {
                sagas = cached
                Result.success(cached)
            } else {
                netResult
            }
        }
    }
}
