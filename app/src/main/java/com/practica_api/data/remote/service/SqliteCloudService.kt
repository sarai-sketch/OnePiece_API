package com.practica_api.data.remote.service

import com.practica_api.data.remote.dto.EliminarFavoritoRequest
import com.practica_api.data.remote.dto.FavoritoRequest
import com.practica_api.data.remote.dto.SqliteCloudRequest
import com.practica_api.data.remote.dto.SqliteCloudResponse
import retrofit2.Response
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
    suspend fun agregarFavorito(
        @Header("Authorization") bearerToken: String,
        @Query("limit") limit: Int = 10,
        @Body favorito: FavoritoRequest
    ): Response<Any>

    @POST("v2/functions/eliminar-favorito")
    suspend fun eliminarFavorito(
        @Header("Authorization") bearerToken: String,
        @Query("limit") limit: Int = 10,
        @Body request: EliminarFavoritoRequest
    ): Response<Any>
}
