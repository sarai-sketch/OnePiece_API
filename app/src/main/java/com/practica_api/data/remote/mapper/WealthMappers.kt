package com.practica_api.data.remote.mapper

import com.practica_api.core.cleaned
import com.practica_api.data.remote.dto.WealthResponseDto
import com.practica_api.domain.model.WealthComparison

fun WealthResponseDto.toDomain() = WealthComparison(
    yoursSentence = data?.resultYours?.sentence.cleaned(),
    theirsSentence = data?.resultTheirs?.sentence.cleaned(),
    theirsScale = data?.resultTheirs?.scale.cleaned(),
    ratioSentence = data?.ratio?.sentence.cleaned(),
    ratioScale = data?.ratio?.scale.cleaned(),
)
