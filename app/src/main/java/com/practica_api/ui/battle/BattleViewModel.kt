package com.practica_api.ui.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.practica_api.core.UiState
import com.practica_api.data.repository.BandaPirata
import com.practica_api.data.repository.BattleCloudRepository
import com.practica_api.data.repository.ResultadoBatalla
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BattleViewModel(
    private val battleRepository: BattleCloudRepository
) : ViewModel() {

    private val _battleState = MutableStateFlow<UiState<ResultadoBatalla>>(UiState.Idle)
    val battleState: StateFlow<UiState<ResultadoBatalla>> = _battleState.asStateFlow()

    val bandas = battleRepository.bandasPredefinidas

    fun simularBatalla(
        bandaNombre: String,
        barco: String,
        capitan: String,
        recompensa: Long
    ) {
        viewModelScope.launch {
            _battleState.value = UiState.Loading
            val miBanda = BandaPirata(
                id = "0",
                nombreBanda = bandaNombre,
                barcoPrincipal = barco,
                capitanNombre = capitan,
                recompensaEstimada = recompensa,
                territorioClave = "Grand Line"
            )
            val result = battleRepository.simularBatallaNaval(miBanda)
            if (result.isSuccess) {
                _battleState.value = UiState.Success(result.getOrThrow())
            } else {
                _battleState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Error al conectar con SQLite Cloud API")
            }
        }
    }
}

class BattleViewModelFactory(
    private val repository: BattleCloudRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        BattleViewModel(repository) as T
}
