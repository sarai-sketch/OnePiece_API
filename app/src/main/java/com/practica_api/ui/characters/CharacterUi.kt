package com.practica_api.ui.characters

import com.practica_api.domain.model.CharacterStatus
import com.practica_api.domain.model.OnePieceCharacter
import java.text.NumberFormat
import java.util.Locale

internal fun OnePieceCharacter.bountyLabel(): String? = bounty?.let {
    "${NumberFormat.getInstance(Locale.forLanguageTag("es")).format(it)} berries"
}

internal fun OnePieceCharacter.statusLabel(): String = when (status) {
    CharacterStatus.ALIVE -> "Vivo"
    CharacterStatus.DECEASED -> "Fallecido"
    CharacterStatus.UNKNOWN -> "Desconocido"
}

internal fun OnePieceCharacter.detailsText(): String = listOfNotNull(
    crew?.let { "Tripulación: ${it.name}" },
    job?.let { "Cargo: $it" },
    height?.let { "Estatura: $it" },
    age?.let { "Edad: $it" },
    bountyLabel()?.let { "Recompensa: $it" },
    "Estado: ${statusLabel()}",
    fruit?.let { "Fruta del Diablo: ${it.name} (${it.type})" },
).joinToString("\n")
