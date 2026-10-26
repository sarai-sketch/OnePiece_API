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
