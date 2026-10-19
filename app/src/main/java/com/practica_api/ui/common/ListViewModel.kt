package com.practica_api.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.practica_api.core.UiState
import com.practica_api.core.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel genérico para pantallas de lista con búsqueda local.
 * Una sola implementación sirve para personajes, frutas y sagas.
 */
class ListViewModel<T>(
    private val fetch: suspend (forceRefresh: Boolean) -> Result<List<T>>,
    private val matches: (item: T, query: String) -> Boolean,
) : ViewModel() {

    private val source = MutableStateFlow<UiState<List<T>>>(UiState.Loading)
    private val query = MutableStateFlow("")

    val state: StateFlow<UiState<List<T>>> = combine(source, query) { current, q ->
        val text = q.trim()
        if (current is UiState.Success && text.isNotEmpty()) {
            UiState.Success(current.data.filter { matches(it, text) })
        } else {
            current
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    init {
        load()
    }

    fun load(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            source.value = UiState.Loading
            source.value = fetch(forceRefresh).toUiState()
        }
    }

    fun onQueryChanged(value: String) {
        query.value = value
    }
}

class ListViewModelFactory<T>(
    private val fetch: suspend (Boolean) -> Result<List<T>>,
    private val matches: (T, String) -> Boolean,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM =
        ListViewModel(fetch, matches) as VM
}
