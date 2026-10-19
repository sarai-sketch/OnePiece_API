package com.practica_api.ui.fruits

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practica_api.R
import com.practica_api.di.AppContainer
import com.practica_api.domain.model.DevilFruit
import com.practica_api.ui.common.BaseListFragment
import com.practica_api.ui.common.ListViewModel
import com.practica_api.ui.common.ListViewModelFactory

class FruitsFragment : BaseListFragment<DevilFruit>() {

    override val searchHint = R.string.search_fruits

    override val viewModel by viewModels<ListViewModel<DevilFruit>> {
        ListViewModelFactory(
            fetch = { refresh -> AppContainer.onePieceRepository.getFruits(refresh) },
            matches = { item, query ->
                listOf(item.name, item.romanName, item.type)
                    .any { it?.contains(query, ignoreCase = true) == true }
            },
        )
    }

    override fun createAdapter(): ListAdapter<DevilFruit, out RecyclerView.ViewHolder> =
        FruitAdapter { fruit ->
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
        }
}
