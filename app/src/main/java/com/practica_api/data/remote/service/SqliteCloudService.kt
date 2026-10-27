package com.practica_api.data.remote.service

import com.practica_api.data.remote.dto.EdgeFunctionEliminarFavoritoRequest
import com.practica_api.data.remote.dto.EdgeFunctionFavoritoRequest
import com.practica_api.data.remote.dto.SqliteCloudRequest
import com.practica_api.data.remote.dto.SqliteCloudResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SqliteCloudService {

    @POST("v2/webrequest")
    suspend fun executeQuery(
        @Header("Authorization") bearerToken: String,
        @Body request: SqliteCloudRequest
    ): SqliteCloudResponse

    @POST("v2/functions/function-1")
    suspend fun invokeEdgeFunction(
        @Header("Authorization") bearerToken: String,
        @Query("limit") limit: Int = 10,
        @Body request: EdgeFunctionFavoritoRequest
    ): SqliteCloudResponse

    @POST("v2/functions/eliminar-favorito")
    suspend fun invokeEliminarFavoritoEdgeFunction(
        @Header("Authorization") bearerToken: String,
        @Query("limit") limit: Int = 10,
        @Body request: EdgeFunctionEliminarFavoritoRequest
    ): SqliteCloudResponse
}
