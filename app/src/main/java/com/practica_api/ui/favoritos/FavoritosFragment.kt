package com.practica_api.ui.favoritos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.practica_api.R
import com.practica_api.core.UiState
import com.practica_api.core.collectFlow
import com.practica_api.databinding.FragmentListBinding
import com.practica_api.di.AppContainer

class FavoritosFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoritosViewModel> {
        FavoritosViewModelFactory(AppContainer.getFavoritosRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FavoritoAdapter { item ->
            viewModel.eliminarFavorito(item)
        }

        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter
        binding.searchLayout.hint = getString(R.string.search_favorites)
        binding.searchInput.doAfterTextChanged {
            viewModel.onQueryChanged(it?.toString().orEmpty())
        }
        binding.retryButton.isVisible = false

        collectFlow(viewModel.state) { state ->
            val items = (state as? UiState.Success)?.data ?: emptyList()
            adapter.submitList(items)
            binding.progress.isVisible = state is UiState.Loading
            binding.emptyText.text = getString(R.string.empty_favorites)
            binding.emptyText.isVisible = (state is UiState.Success) && items.isEmpty()
        }
    }

    override fun onDestroyView() {
        binding.list.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
