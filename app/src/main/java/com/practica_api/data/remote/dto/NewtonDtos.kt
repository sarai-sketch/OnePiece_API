package com.practica_api.data.remote.dto

data class NewtonDto(
    val operation: String? = null,
    val expression: String? = null,
    val result: String? = null,
    val error: String? = null,
)
