package com.practica_api.ui.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practica_api.core.UiState
import com.practica_api.core.toUiState
import com.practica_api.data.repository.NewtonRepository
import com.practica_api.data.repository.WealthRepository
import com.practica_api.domain.model.NewtonResult
import com.practica_api.domain.model.WealthComparison
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ToolsViewModel(
    private val newton: NewtonRepository,
    private val wealth: WealthRepository,
) : ViewModel() {

    private val _newtonState = MutableStateFlow<UiState<NewtonResult>>(UiState.Idle)
    val newtonState: StateFlow<UiState<NewtonResult>> = _newtonState.asStateFlow()

    private val _wealthState = MutableStateFlow<UiState<WealthComparison>>(UiState.Idle)
    val wealthState: StateFlow<UiState<WealthComparison>> = _wealthState.asStateFlow()

    private var newtonJob: Job? = null
    private var wealthJob: Job? = null

    fun calculate(operation: String, expression: String) {
        newtonJob?.cancel()
        newtonJob = viewModelScope.launch {
            _newtonState.value = UiState.Loading
            _newtonState.value = newton.calculate(operation, expression).toUiState()
        }
    }

    fun compareWealth(yours: String, theirs: String, spend: String) {
        wealthJob?.cancel()
        wealthJob = viewModelScope.launch {
            _wealthState.value = UiState.Loading
            _wealthState.value = wealth.compare(yours, theirs, spend).toUiState()
        }
    }
}
