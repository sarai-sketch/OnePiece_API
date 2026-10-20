package com.practica_api.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {

    @Query("SELECT * FROM favoritos ORDER BY agregadoEn DESC")
    fun observarFavoritos(): Flow<List<FavoritoEntity>>

    @Query("SELECT idKey FROM favoritos")
    fun observarIdsFavoritos(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(favorito: FavoritoEntity)

    @Query("DELETE FROM favoritos WHERE idKey = :idKey")
    suspend fun eliminarPorIdKey(idKey: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE idKey = :idKey)")
    suspend fun esFavorito(idKey: String): Boolean
}
