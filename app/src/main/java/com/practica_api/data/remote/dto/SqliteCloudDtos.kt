package com.practica_api.data.remote.dto

data class FavoritoRequest(
    val id_key: String,
    val original_id: Int,
    val tipo: String,
    val nombre: String,
    val subtitulo: String,
    val imagen_url: String,
    val detalle_info: String
)

data class EliminarFavoritoRequest(
    val id_key: String
)

data class SqliteCloudRequest(
    val sql: String,
    val database: String = "auth.sqlitecloud"
)

data class SqliteCloudResponse(
    val status: String? = null,
    val data: List<Map<String, Any?>>? = null,
    val error: String? = null
)
