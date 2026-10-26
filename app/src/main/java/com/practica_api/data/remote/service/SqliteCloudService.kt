package com.practica_api.data.remote.service

import com.practica_api.data.remote.dto.SqliteCloudRequest
import com.practica_api.data.remote.dto.SqliteCloudResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SqliteCloudService {

    @POST("v2/webrequest")
    suspend fun executeQuery(
        @Header("Authorization") bearerToken: String,
        @Body request: SqliteCloudRequest
    ): SqliteCloudResponse
}
