package com.practica_api.domain.model

data class NewtonResult(
    val operation: String,
    val expression: String,
    val result: String?,
)

data class WealthComparison(
    val yoursSentence: String?,
    val theirsSentence: String?,
    val theirsScale: String?,
    val ratioSentence: String?,
    val ratioScale: String?,
)
