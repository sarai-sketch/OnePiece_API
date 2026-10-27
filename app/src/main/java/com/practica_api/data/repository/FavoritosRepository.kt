package com.practica_api.data.repository

import com.practica_api.data.local.FavoritoDao
import com.practica_api.data.local.FavoritoEntity
import com.practica_api.data.remote.client.RetrofitClient
import com.practica_api.data.remote.dto.EdgeFunctionEliminarFavoritoRequest
import com.practica_api.data.remote.dto.EdgeFunctionFavoritoRequest
import com.practica_api.data.remote.dto.SqliteCloudResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoritosRepository(private val dao: FavoritoDao) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun observarFavoritos(): Flow<List<FavoritoEntity>> = dao.observarFavoritos()

    fun observarIds(): Flow<Set<String>> = dao.observarIdsFavoritos().map { it.toSet() }

    suspend fun alternarFavorito(
        idKey: String,
        originalId: Int,
        tipo: String,
        nombre: String,
        subtitulo: String,
        imagenUrl: String? = null,
        detalleInfo: String? = null,
    ) {
        if (dao.esFavorito(idKey)) {
            dao.eliminarPorIdKey(idKey)
            scope.launch {
                eliminarEnEdgeFunction(idKey)
            }
        } else {
            val entity = FavoritoEntity(
                idKey = idKey,
                originalId = originalId,
                tipo = tipo,
                nombre = nombre,
                subtitulo = subtitulo,
                imagenUrl = imagenUrl,
                detalleInfo = detalleInfo,
            )
            dao.insertar(entity)

            scope.launch {
                enviarAEdgeFunction(
                    idKey = idKey,
                    originalId = originalId,
                    tipo = tipo,
                    nombre = nombre,
                    subtitulo = subtitulo,
                    imagenUrl = imagenUrl,
                    detalleInfo = detalleInfo,
                )
            }
        }
    }

    suspend fun eliminar(idKey: String) {
        dao.eliminarPorIdKey(idKey)
        scope.launch {
            eliminarEnEdgeFunction(idKey)
        }
    }

    suspend fun enviarAEdgeFunction(
        idKey: String,
        originalId: Int,
        tipo: String,
        nombre: String,
        subtitulo: String? = null,
        imagenUrl: String? = null,
        detalleInfo: String? = null,
        limit: Int = 10,
    ): Result<SqliteCloudResponse> {
        return try {
            val req = EdgeFunctionFavoritoRequest(
                id_key = idKey,
                original_id = originalId,
                tipo = tipo,
                nombre = nombre,
                subtitulo = subtitulo,
                imagen_url = imagenUrl,
                detalle_info = detalleInfo,
            )
            val response = RetrofitClient.edgeFunctionService.invokeEdgeFunction(
                bearerToken = RetrofitClient.SQLITE_CLOUD_API_KEY,
                limit = limit,
                request = req,
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarEnEdgeFunction(
        idKey: String,
        limit: Int = 10,
    ): Result<SqliteCloudResponse> {
        return try {
            val req = EdgeFunctionEliminarFavoritoRequest(id_key = idKey)
            val response = RetrofitClient.edgeFunctionService.invokeEliminarFavoritoEdgeFunction(
                bearerToken = RetrofitClient.SQLITE_CLOUD_API_KEY,
                limit = limit,
                request = req,
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
