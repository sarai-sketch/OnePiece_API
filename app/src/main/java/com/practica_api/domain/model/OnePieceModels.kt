package com.practica_api.domain.model

enum class CharacterStatus { ALIVE, DECEASED, UNKNOWN }

data class Crew(
    val name: String,
    val romanName: String?,
    val isYonko: Boolean,
)

data class DevilFruit(
    val id: Int,
    val name: String,
    val romanName: String?,
    val type: String,
    val description: String?,
    val imageUrl: String?,
)

data class OnePieceCharacter(
    val id: Int,
    val name: String,
    val job: String?,
    val height: String?,
    val age: String?,
    val bounty: Long?,
    val crew: Crew?,
    val fruit: DevilFruit?,
    val status: CharacterStatus,
)

data class Saga(
    val id: Int,
    val number: String,
    val title: String,
    val chapters: String,
    val volumes: String,
    val episodes: String,
)
