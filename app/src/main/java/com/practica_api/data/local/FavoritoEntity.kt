package com.practica_api.data.local

data class FavoritoEntity(
    val idKey: String,
    val originalId: Int,
    val tipo: String,
    val nombre: String,
    val subtitulo: String,
    val imagenUrl: String? = null,
    val detalleInfo: String? = null,
    val agregadoEn: Long = System.currentTimeMillis()
)
