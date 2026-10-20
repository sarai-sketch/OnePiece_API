package com.practica_api.ui.fruits

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
import com.practica_api.domain.model.DevilFruit
import com.practica_api.ui.common.BaseListFragment
import com.practica_api.ui.common.ListViewModel
import com.practica_api.ui.common.ListViewModelFactory
import kotlinx.coroutines.launch

class FruitsFragment : BaseListFragment<DevilFruit>() {

    override val searchHint = R.string.search_fruits
    private var fruitAdapter: FruitAdapter? = null

    override val viewModel by viewModels<ListViewModel<DevilFruit>> {
        ListViewModelFactory(
            fetch = { refresh -> AppContainer.onePieceRepository.getFruits(refresh) },
            matches = { item, query ->
                listOf(item.name, item.romanName, item.type)
                    .any { it?.contains(query, ignoreCase = true) == true }
            },
        )
    }

    override fun createAdapter(): ListAdapter<DevilFruit, out RecyclerView.ViewHolder> {
        val adapter = FruitAdapter(
            onClick = { fruit ->
                val message = listOfNotNull(
                    fruit.romanName,
                    "Tipo: ${fruit.type}",
                    fruit.description,
                ).joinToString("\n\n")
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(fruit.name)
                    .setMessage(message)
                    .setPositiveButton(R.string.close, null)
                    .show()
            },
            onFavoriteClick = { fruit ->
                lifecycleScope.launch {
                    AppContainer.getFavoritosRepository(requireContext()).alternarFavorito(
                        idKey = "FRUIT_${fruit.id}",
                        originalId = fruit.id,
                        tipo = "FRUTA",
                        nombre = fruit.name,
                        subtitulo = "Tipo: ${fruit.type}",
                        imagenUrl = fruit.imageUrl,
                        detalleInfo = fruit.description
                    )
                }
            }
        )
        fruitAdapter = adapter
        return adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlow(AppContainer.getFavoritosRepository(requireContext()).observarIds()) { ids ->
            fruitAdapter?.updateFavoritos(ids)
        }
    }
}
