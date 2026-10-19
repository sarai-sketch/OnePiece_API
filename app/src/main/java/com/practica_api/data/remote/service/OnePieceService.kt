package com.practica_api.data.remote.service

import com.practica_api.data.remote.dto.CharacterDto
import com.practica_api.data.remote.dto.FruitDto
import com.practica_api.data.remote.dto.SagaDto
import retrofit2.http.GET

interface OnePieceService {
    @GET("characters/en")
    suspend fun getCharacters(): List<CharacterDto>

    @GET("fruits/en")
    suspend fun getFruits(): List<FruitDto>

    @GET("sagas/en")
    suspend fun getSagas(): List<SagaDto>
}
