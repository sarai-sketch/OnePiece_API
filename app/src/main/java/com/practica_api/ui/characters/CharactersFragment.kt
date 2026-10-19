package com.practica_api.ui.characters

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practica_api.R
import com.practica_api.di.AppContainer
import com.practica_api.domain.model.OnePieceCharacter
import com.practica_api.ui.common.BaseListFragment
import com.practica_api.ui.common.ListViewModel
import com.practica_api.ui.common.ListViewModelFactory

class CharactersFragment : BaseListFragment<OnePieceCharacter>() {

    override val searchHint = R.string.search_characters

    override val viewModel by viewModels<ListViewModel<OnePieceCharacter>> {
        ListViewModelFactory(
            fetch = { refresh -> AppContainer.onePieceRepository.getCharacters(refresh) },
            matches = { item, query ->
                listOf(item.name, item.job, item.crew?.name, item.fruit?.name)
                    .any { it?.contains(query, ignoreCase = true) == true }
            },
        )
    }

    override fun createAdapter(): ListAdapter<OnePieceCharacter, out RecyclerView.ViewHolder> =
        CharacterAdapter { character ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(character.name)
                .setMessage(character.detailsText())
                .setPositiveButton(R.string.close, null)
                .show()
        }
}
