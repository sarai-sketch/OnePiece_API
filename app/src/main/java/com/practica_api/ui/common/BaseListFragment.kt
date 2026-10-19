package com.practica_api.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practica_api.core.UiState
import com.practica_api.core.collectFlow
import com.practica_api.databinding.FragmentListBinding

/** Pantalla de lista reutilizable: buscador, cargando, error con reintento y lista vacía. */
abstract class BaseListFragment<T> : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    protected abstract val viewModel: ListViewModel<T>
    @get:StringRes
    protected abstract val searchHint: Int
    protected abstract fun createAdapter(): ListAdapter<T, out RecyclerView.ViewHolder>

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
        val adapter = createAdapter()

        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter
        binding.searchLayout.hint = getString(searchHint)
        binding.searchInput.doAfterTextChanged { viewModel.onQueryChanged(it?.toString().orEmpty()) }
        binding.retryButton.setOnClickListener { viewModel.load(forceRefresh = true) }

        collectFlow(viewModel.state) { render(it, adapter) }
    }

    private fun render(state: UiState<List<T>>, adapter: ListAdapter<T, out RecyclerView.ViewHolder>) {
        val items = if (state is UiState.Success) state.data else emptyList()
        adapter.submitList(items)
        with(binding) {
            progress.isVisible = state is UiState.Loading
            errorGroup.isVisible = state is UiState.Error
            if (state is UiState.Error) errorText.text = state.message
            emptyText.isVisible = state is UiState.Success && items.isEmpty()
        }
    }

    override fun onDestroyView() {
        binding.list.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
