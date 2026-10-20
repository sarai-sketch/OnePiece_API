package com.practica_api.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterCacheDao {
    @Query("SELECT * FROM character_cache ORDER BY id ASC")
    fun observarTodos(): Flow<List<CharacterCacheEntity>>

    @Query("SELECT * FROM character_cache ORDER BY id ASC")
    suspend fun obtenerTodos(): List<CharacterCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(items: List<CharacterCacheEntity>)

    @Query("DELETE FROM character_cache")
    suspend fun borrarTodos()
}

@Dao
interface FruitCacheDao {
    @Query("SELECT * FROM fruit_cache ORDER BY id ASC")
    fun observarTodos(): Flow<List<FruitCacheEntity>>

    @Query("SELECT * FROM fruit_cache ORDER BY id ASC")
    suspend fun obtenerTodos(): List<FruitCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(items: List<FruitCacheEntity>)

    @Query("DELETE FROM fruit_cache")
    suspend fun borrarTodos()
}

@Dao
interface SagaCacheDao {
    @Query("SELECT * FROM saga_cache ORDER BY id ASC")
    fun observarTodos(): Flow<List<SagaCacheEntity>>

    @Query("SELECT * FROM saga_cache ORDER BY id ASC")
    suspend fun obtenerTodos(): List<SagaCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(items: List<SagaCacheEntity>)

    @Query("DELETE FROM saga_cache")
    suspend fun borrarTodos()
}
