package com.practica_api.ui.battle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practica_api.R
import com.practica_api.core.UiState
import com.practica_api.core.cleaned
import com.practica_api.core.collectFlow
import com.practica_api.databinding.FragmentBattleBinding
import com.practica_api.di.AppContainer

class BattleFragment : Fragment() {

    private var _binding: FragmentBattleBinding? = null
    private val binding get() = _binding!!

    private var recompensaActual: Long = 3000000000L

    private val viewModel by viewModels<BattleViewModel> {
        BattleViewModelFactory(AppContainer.battleCloudRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBattleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCrewDropdown()

        binding.btnStartBattle.setOnClickListener { onStartBattle() }

        collectFlow(viewModel.battleState) { state ->
            binding.battleProgress.isVisible = state is UiState.Loading
            binding.btnStartBattle.isEnabled = state !is UiState.Loading

            binding.battleResult.text = when (state) {
                is UiState.Success -> {
                    val res = state.data
                    val title = if (res.esVictoria) "🏆 ¡VICTORIA EN ALTA MAR!" else "💥 ¡DERROTA NAVAL!"
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(title)
                        .setMessage(res.detalleCombate)
                        .setPositiveButton(R.string.close, null)
                        .show()

                    res.detalleCombate
                }
                is UiState.Error -> state.message
                else -> ""
            }
        }
    }

    private fun setupCrewDropdown() {
        val nombresBandas = viewModel.bandas.map { it.nombreBanda }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, nombresBandas)
        binding.crewAutoComplete.setAdapter(adapter)

        binding.crewAutoComplete.setText(nombresBandas.first(), false)

        binding.crewAutoComplete.setOnItemClickListener { _, _, position, _ ->
            val seleccionada = viewModel.bandas[position]
            binding.shipNameInput.setText(seleccionada.barcoPrincipal)
            binding.captainNameInput.setText(seleccionada.capitanNombre)
            recompensaActual = seleccionada.recompensaEstimada
            binding.bountyDisplay.text = getString(R.string.battle_bounty_format, recompensaActual)
        }
    }

    private fun onStartBattle() {
        val crewName = binding.crewAutoComplete.text.toString().cleaned() ?: "Sombrero de Paja"
        val shipName = binding.shipNameInput.text?.toString().cleaned() ?: "Thousand Sunny"
        val captainName = binding.captainNameInput.text?.toString().cleaned() ?: "Monkey D. Luffy"

        viewModel.simularBatalla(crewName, shipName, captainName, recompensaActual)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
