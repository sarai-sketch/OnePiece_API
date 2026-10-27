package com.practica_api.data.repository

import com.practica_api.data.local.FavoritoEntity
import com.practica_api.data.remote.client.RetrofitClient
import com.practica_api.data.remote.dto.EliminarFavoritoRequest
import com.practica_api.data.remote.dto.FavoritoRequest
import com.practica_api.data.remote.service.SqliteCloudService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosRepository(
    private val service: SqliteCloudService = RetrofitClient.edgeFunctionService,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _favoritos = MutableStateFlow<List<FavoritoEntity>>(emptyList())
    private val _favoritosIds = MutableStateFlow<Set<String>>(emptySet())

    val favoritos: StateFlow<List<FavoritoEntity>> = _favoritos.asStateFlow()
    val favoritosIds: StateFlow<Set<String>> = _favoritosIds.asStateFlow()

    fun observarFavoritos(): Flow<List<FavoritoEntity>> = favoritos

    fun observarIds(): Flow<Set<String>> = favoritosIds

    fun alternarFavorito(
        idKey: String,
        originalId: Int,
        tipo: String,
        nombre: String,
        subtitulo: String,
        imagenUrl: String? = null,
        detalleInfo: String? = null,
    ) {
        val actualIds = _favoritosIds.value.toMutableSet()
        val esta = actualIds.contains(idKey)

        if (esta) {
            actualIds.remove(idKey)
            _favoritosIds.value = actualIds
            _favoritos.value = _favoritos.value.filterNot { it.idKey == idKey }

            scope.launch {
                try {
                    service.eliminarFavorito(
                        bearerToken = RetrofitClient.SQLITE_CLOUD_API_KEY,
                        request = EliminarFavoritoRequest(id_key = idKey),
                    )
                } catch (_: Exception) {}
            }
        } else {
            actualIds.add(idKey)
            _favoritosIds.value = actualIds

            val entity = FavoritoEntity(
                idKey = idKey,
                originalId = originalId,
                tipo = tipo,
                nombre = nombre,
                subtitulo = subtitulo,
                imagenUrl = imagenUrl,
                detalleInfo = detalleInfo,
            )
            _favoritos.value = listOf(entity) + _favoritos.value.filterNot { it.idKey == idKey }

            scope.launch {
                try {
                    val req = FavoritoRequest(
                        id_key = idKey,
                        original_id = originalId,
                        tipo = tipo,
                        nombre = nombre,
                        subtitulo = subtitulo,
                        imagen_url = imagenUrl.orEmpty(),
                        detalle_info = detalleInfo.orEmpty(),
                    )
                    service.agregarFavorito(
                        bearerToken = RetrofitClient.SQLITE_CLOUD_API_KEY,
                        favorito = req,
                    )
                } catch (_: Exception) {}
            }
        }
    }

    fun eliminar(idKey: String) {
        val actualIds = _favoritosIds.value.toMutableSet()
        actualIds.remove(idKey)
        _favoritosIds.value = actualIds
        _favoritos.value = _favoritos.value.filterNot { it.idKey == idKey }

        scope.launch {
            try {
                service.eliminarFavorito(
                    bearerToken = RetrofitClient.SQLITE_CLOUD_API_KEY,
                    request = EliminarFavoritoRequest(id_key = idKey),
                )
            } catch (_: Exception) {}
        }
    }
}
