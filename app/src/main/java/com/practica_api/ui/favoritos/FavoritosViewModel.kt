package com.practica_api.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.practica_api.core.UiState
import com.practica_api.data.local.FavoritoEntity
import com.practica_api.data.repository.FavoritosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val repository: FavoritosRepository
) : ViewModel() {

    private val query = MutableStateFlow("")

    val state: StateFlow<UiState<List<FavoritoEntity>>> = combine(
        repository.observarFavoritos(),
        query
    ) { lista, q ->
        val text = q.trim()
        val filtrados = if (text.isEmpty()) {
            lista
        } else {
            lista.filter {
                it.nombre.contains(text, ignoreCase = true) ||
                        it.subtitulo.contains(text, ignoreCase = true) ||
                        it.tipo.contains(text, ignoreCase = true)
            }
        }
        UiState.Success(filtrados)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery
    }

    fun eliminarFavorito(favorito: FavoritoEntity) {
        viewModelScope.launch {
            repository.eliminar(favorito.idKey)
        }
    }
}

class FavoritosViewModelFactory(
    private val repository: FavoritosRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        FavoritosViewModel(repository) as T
}
