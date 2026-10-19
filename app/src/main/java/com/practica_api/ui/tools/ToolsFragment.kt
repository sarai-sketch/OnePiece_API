package com.practica_api.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.material.textfield.TextInputLayout
import com.practica_api.R
import com.practica_api.core.UiState
import com.practica_api.core.cleaned
import com.practica_api.core.collectFlow
import com.practica_api.databinding.FragmentToolsBinding
import com.practica_api.di.AppContainer
import com.practica_api.domain.model.NewtonResult
import com.practica_api.domain.model.WealthComparison

class ToolsFragment : Fragment() {

    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ToolsViewModel> {
        viewModelFactory {
            initializer {
                ToolsViewModel(AppContainer.newtonRepository, AppContainer.wealthRepository)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newtonButton.setOnClickListener { onCalculate() }
        binding.wealthButton.setOnClickListener { onCompare() }

        collectFlow(viewModel.newtonState) { renderNewton(it) }
        collectFlow(viewModel.wealthState) { renderWealth(it) }
    }

    private fun onCalculate() {
        val expression = binding.expressionInput.text?.toString().cleaned()
        if (!binding.expressionLayout.requireValue(expression)) return

        val operation = when (binding.operationGroup.checkedChipId) {
            R.id.chipIntegrate -> "integrate"
            R.id.chipSimplify -> "simplify"
            R.id.chipFactor -> "factor"
            else -> "derive"
        }
        viewModel.calculate(operation, expression!!)
    }

    private fun onCompare() {
        val yours = binding.yoursInput.text?.toString().cleaned()
        val theirs = binding.theirsInput.text?.toString().cleaned()
        val spend = binding.spendInput.text?.toString().cleaned()

        // Se evalúan los tres para marcar todos los errores a la vez.
        val valid = listOf(
            binding.yoursLayout.requireValue(yours),
            binding.theirsLayout.requireValue(theirs),
            binding.spendLayout.requireValue(spend),
        ).all { it }
        if (!valid) return

        viewModel.compareWealth(yours!!, theirs!!, spend!!)
    }

    private fun TextInputLayout.requireValue(value: String?): Boolean {
        error = if (value == null) getString(R.string.error_required) else null
        return value != null
    }

    private fun renderNewton(state: UiState<NewtonResult>) {
        binding.newtonProgress.isVisible = state is UiState.Loading
        binding.newtonButton.isEnabled = state !is UiState.Loading
        binding.newtonResult.text = when (state) {
            is UiState.Success -> {
                val data = state.data
                data.result?.let {
                    getString(R.string.newton_result, data.operation, data.expression, it)
                } ?: getString(R.string.newton_no_result)
            }
            is UiState.Error -> state.message
            else -> ""
        }
    }

    private fun renderWealth(state: UiState<WealthComparison>) {
        binding.wealthProgress.isVisible = state is UiState.Loading
        binding.wealthButton.isEnabled = state !is UiState.Loading
        binding.wealthResult.text = when (state) {
            is UiState.Success -> {
                val data = state.data
                listOfNotNull(
                    data.yoursSentence,
                    data.theirsSentence,
                    data.theirsScale,
                    data.ratioSentence,
                    data.ratioScale,
                ).joinToString("\n\n").ifEmpty { getString(R.string.wealth_no_result) }
            }
            is UiState.Error -> state.message
            else -> ""
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
