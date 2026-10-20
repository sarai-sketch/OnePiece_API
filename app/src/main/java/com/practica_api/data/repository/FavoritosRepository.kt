package com.practica_api.data.repository

import com.practica_api.data.local.FavoritoDao
import com.practica_api.data.local.FavoritoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritosRepository(private val dao: FavoritoDao) {

    fun observarFavoritos(): Flow<List<FavoritoEntity>> = dao.observarFavoritos()

    fun observarIds(): Flow<Set<String>> = dao.observarIdsFavoritos().map { it.toSet() }

    suspend fun alternarFavorito(
        idKey: String,
        originalId: Int,
        tipo: String,
        nombre: String,
        subtitulo: String,
        imagenUrl: String? = null,
        detalleInfo: String? = null
    ) {
        if (dao.esFavorito(idKey)) {
            dao.eliminarPorIdKey(idKey)
        } else {
            dao.insertar(
                FavoritoEntity(
                    idKey = idKey,
                    originalId = originalId,
                    tipo = tipo,
                    nombre = nombre,
                    subtitulo = subtitulo,
                    imagenUrl = imagenUrl,
                    detalleInfo = detalleInfo
                )
            )
        }
    }

    suspend fun eliminar(idKey: String) = dao.eliminarPorIdKey(idKey)
}
