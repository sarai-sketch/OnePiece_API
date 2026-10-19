package com.practica_api.data.remote.dto

data class WealthResponseDto(
    val status: String? = null,
    val data: WealthDataDto? = null,
)

data class WealthDataDto(
    val expression: String? = null,
    val resultTheirs: WealthResultDto? = null,
    val resultYours: WealthResultDto? = null,
    val ratio: WealthRatioDto? = null,
)

data class WealthResultDto(
    val value: Double? = null,
    val unit: String? = null,
    val phrase: String? = null,
    val sentence: String? = null,
    val scale: String? = null,
)

data class WealthRatioDto(
    val value: Double? = null,
    val phrase: String? = null,
    val sentence: String? = null,
    val scale: String? = null,
)
