package com.practica_api.data.remote.dto

data class SqliteCloudRequest(
    val sql: String,
    val database: String = "auth.sqlitecloud"
)

data class SqliteCloudResponse(
    val status: String? = null,
    val data: List<Map<String, Any?>>? = null,
    val error: String? = null
)

data class EdgeFunctionFavoritoRequest(
    val id_key: String,
    val original_id: Int,
    val tipo: String,
    val nombre: String,
    val subtitulo: String? = null,
    val imagen_url: String? = null,
    val detalle_info: String? = null
)

data class EdgeFunctionEliminarFavoritoRequest(
    val id_key: String
)
