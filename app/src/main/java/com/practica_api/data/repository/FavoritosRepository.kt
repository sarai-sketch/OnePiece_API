package com.practica_api.data.repository

import com.practica_api.data.local.FavoritoDao
import com.practica_api.data.local.FavoritoEntity
import com.practica_api.data.remote.client.RetrofitClient
import com.practica_api.data.remote.dto.EdgeFunctionEliminarFavoritoRequest
import com.practica_api.data.remote.dto.EdgeFunctionFavoritoRequest
import com.practica_api.data.remote.dto.SqliteCloudRequest
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
                eliminarEnSqliteCloud(idKey)
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
                guardarEnSqliteCloud(
                    idKey = idKey,
                    originalId = originalId,
                    tipo = tipo,
                    nombre = nombre,
                    subtitulo = subtitulo,
                    imagenUrl = imagenUrl,
                    detalleInfo = detalleInfo,
                )
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
            eliminarEnSqliteCloud(idKey)
            eliminarEnEdgeFunction(idKey)
        }
    }

    private suspend fun guardarEnSqliteCloud(
        idKey: String,
        originalId: Int,
        tipo: String,
        nombre: String,
        subtitulo: String? = null,
        imagenUrl: String? = null,
        detalleInfo: String? = null,
    ) {
        try {
            val safeNombre = nombre.replace("'", "''")
            val safeSub = (subtitulo ?: "").replace("'", "''")
            val safeImg = (imagenUrl ?: "").replace("'", "''")
            val safeDet = (detalleInfo ?: "").replace("'", "''")
            val sql = """
                CREATE TABLE IF NOT EXISTS favoritos (
                    id_key TEXT PRIMARY KEY,
                    original_id INTEGER NOT NULL,
                    tipo TEXT NOT NULL,
                    nombre TEXT NOT NULL,
                    subtitulo TEXT,
                    imagen_url TEXT,
                    detalle_info TEXT,
                    agregado_en INTEGER
                );
                INSERT OR REPLACE INTO favoritos (id_key, original_id, tipo, nombre, subtitulo, imagen_url, detalle_info, agregado_en)
                VALUES ('$idKey', $originalId, '$tipo', '$safeNombre', '$safeSub', '$safeImg', '$safeDet', ${System.currentTimeMillis()});
            """.trimIndent()

            RetrofitClient.sqliteCloudService.executeQuery(
                RetrofitClient.SQLITE_CLOUD_API_KEY,
                SqliteCloudRequest(sql = sql, database = RetrofitClient.SQLITE_CLOUD_DB_NAME),
            )
        } catch (_: Exception) {
        }
    }

    private suspend fun eliminarEnSqliteCloud(idKey: String) {
        try {
            val sql = "DELETE FROM favoritos WHERE id_key = '$idKey';"
            RetrofitClient.sqliteCloudService.executeQuery(
                RetrofitClient.SQLITE_CLOUD_API_KEY,
                SqliteCloudRequest(sql = sql, database = RetrofitClient.SQLITE_CLOUD_DB_NAME),
            )
        } catch (_: Exception) {
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
