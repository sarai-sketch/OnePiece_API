package com.practica_api.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey val idKey: String, // ej. "CHAR_1", "FRUIT_2", "SAGA_3"
    val originalId: Int,
    val tipo: String, // "PERSONAJE", "FRUTA", "SAGA"
    val nombre: String,
    val subtitulo: String,
    val imagenUrl: String? = null,
    val detalleInfo: String? = null,
    val agregadoEn: Long = System.currentTimeMillis()
)
