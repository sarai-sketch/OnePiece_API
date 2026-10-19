package com.practica_api.data.remote.dto

import com.google.gson.annotations.SerializedName

/** DTOs: reflejan el JSON tal cual lo manda la API (todo nullable, sin lógica). */
data class CharacterDto(
    val id: Int = 0,
    val name: String? = null,
    val size: String? = null,
    val age: String? = null,
    val bounty: String? = null,
    val crew: CrewDto? = null,
    val fruit: FruitDto? = null,
    val job: String? = null,
    val status: String? = null,
)

data class CrewDto(
    val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val number: String? = null,
    @SerializedName("roman_name") val romanName: String? = null,
    @SerializedName("total_prime") val totalPrime: String? = null,
    @SerializedName("is_yonko") val isYonko: Boolean? = null,
)

data class FruitDto(
    val id: Int = 0,
    val name: String? = null,
    val description: String? = null,
    @SerializedName("roman_name") val romanName: String? = null,
    val type: String? = null,
    val filename: String? = null,
)

data class SagaDto(
    val id: Int = 0,
    val title: String? = null,
    @SerializedName("saga_number") val number: String? = null,
    @SerializedName("saga_chapitre") val chapters: String? = null,
    @SerializedName("saga_volume") val volumes: String? = null,
    @SerializedName("saga_episode") val episodes: String? = null,
)
