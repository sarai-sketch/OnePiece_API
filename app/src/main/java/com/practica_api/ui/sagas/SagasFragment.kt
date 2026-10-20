package com.practica_api.ui.sagas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practica_api.R
import com.practica_api.core.collectFlow
import com.practica_api.di.AppContainer
import com.practica_api.domain.model.Saga
import com.practica_api.ui.common.BaseListFragment
import com.practica_api.ui.common.ListViewModel
import com.practica_api.ui.common.ListViewModelFactory
import kotlinx.coroutines.launch

class SagasFragment : BaseListFragment<Saga>() {

    override val searchHint = R.string.search_sagas
    private var sagaAdapter: SagaAdapter? = null

    override val viewModel by viewModels<ListViewModel<Saga>> {
        ListViewModelFactory(
            fetch = { refresh -> AppContainer.onePieceRepository.getSagas(refresh) },
            matches = { item, query ->
                listOf(item.title, item.number, item.episodes, item.chapters)
                    .any { it.contains(query, ignoreCase = true) }
            },
        )
    }

    override fun createAdapter(): ListAdapter<Saga, out RecyclerView.ViewHolder> {
        val adapter = SagaAdapter(
            onClick = { saga ->
                val message = listOf(
                    "Saga #${saga.number}",
                    "Capítulos: ${saga.chapters}",
                    "Tomos: ${saga.volumes}",
                    "Episodios del Anime: ${saga.episodes}"
                ).joinToString("\n\n")
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(saga.title)
                    .setMessage(message)
                    .setPositiveButton(R.string.close, null)
                    .show()
            },
            onFavoriteClick = { saga ->
                lifecycleScope.launch {
                    AppContainer.getFavoritosRepository(requireContext()).alternarFavorito(
                        idKey = "SAGA_${saga.id}",
                        originalId = saga.id,
                        tipo = "SAGA",
                        nombre = saga.title,
                        subtitulo = "Saga #${saga.number}",
                        imagenUrl = null,
                        detalleInfo = "Episodios: ${saga.episodes} | Tomos: ${saga.volumes}"
                    )
                }
            }
        )
        sagaAdapter = adapter
        return adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlow(AppContainer.getFavoritosRepository(requireContext()).observarIds()) { ids ->
            sagaAdapter?.updateFavoritos(ids)
        }
    }
}
