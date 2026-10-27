package com.practica_api.data.remote.mapper

import com.practica_api.core.cleaned
import com.practica_api.data.remote.dto.CharacterDto
import com.practica_api.data.remote.dto.CrewDto
import com.practica_api.data.remote.dto.FruitDto
import com.practica_api.data.remote.dto.SagaDto
import com.practica_api.domain.model.CharacterStatus
import com.practica_api.domain.model.Crew
import com.practica_api.domain.model.DevilFruit
import com.practica_api.domain.model.OnePieceCharacter
import com.practica_api.domain.model.Saga

fun CharacterDto.toDomain() = OnePieceCharacter(
    id = id,
    name = name.cleaned() ?: "Desconocido",
    job = job.cleaned(),
    height = size.cleaned(),
    age = age.cleaned()?.replace(" ans", " años"),
    bounty = bounty.cleaned()?.filter { it.isDigit() }?.toLongOrNull(),
    crew = crew?.toDomain(),
    fruit = fruit?.toDomain(),
    status = status.toCharacterStatus(),
)

fun CrewDto.toDomain(): Crew? = name.cleaned()?.let {
    Crew(name = it, romanName = romanName.cleaned(), isYonko = isYonko == true)
}

fun FruitDto.toDomain() = DevilFruit(
    id = id,
    name = name.cleaned() ?: "Fruta sin nombre",
    romanName = romanName.cleaned(),
    type = type.cleaned() ?: "Desconocido",
    description = description.cleaned(),
    imageUrl = filename.cleaned(),
)

fun SagaDto.toDomain() = Saga(
    id = id,
    number = number.cleaned() ?: "·",
    title = title.cleaned() ?: "Saga sin título",
    chapters = chapters.toRange(),
    volumes = volumes.toRange(),
    episodes = episodes.toRange(),
)

private fun String?.toCharacterStatus(): CharacterStatus = when (this?.trim()?.lowercase()) {
    "living", "vivant", "alive" -> CharacterStatus.ALIVE
    "deceased", "mort", "dead" -> CharacterStatus.DECEASED
    else -> CharacterStatus.UNKNOWN
}

private fun String?.toRange(): String = cleaned()?.replace(" à ", " – ") ?: "—"
