package com.practica_api.data.remote.mapper

import com.practica_api.core.cleaned
import com.practica_api.data.remote.dto.NewtonDto
import com.practica_api.domain.model.NewtonResult

fun NewtonDto.toDomain() = NewtonResult(
    operation = operation.orEmpty(),
    expression = expression.orEmpty(),
    result = result.cleaned(),
)
