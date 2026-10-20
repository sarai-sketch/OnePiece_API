package com.practica_api.ui.characters

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
import com.practica_api.domain.model.OnePieceCharacter
import com.practica_api.ui.common.BaseListFragment
import com.practica_api.ui.common.ListViewModel
import com.practica_api.ui.common.ListViewModelFactory
import kotlinx.coroutines.launch

class CharactersFragment : BaseListFragment<OnePieceCharacter>() {

    override val searchHint = R.string.search_characters
    private var characterAdapter: CharacterAdapter? = null

    override val viewModel by viewModels<ListViewModel<OnePieceCharacter>> {
        ListViewModelFactory(
            fetch = { refresh -> AppContainer.onePieceRepository.getCharacters(refresh) },
            matches = { item, query ->
                listOf(item.name, item.job, item.crew?.name, item.fruit?.name)
                    .any { it?.contains(query, ignoreCase = true) == true }
            },
        )
    }

    override fun createAdapter(): ListAdapter<OnePieceCharacter, out RecyclerView.ViewHolder> {
        val adapter = CharacterAdapter(
            onClick = { character ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(character.name)
                    .setMessage(character.detailsText())
                    .setPositiveButton(R.string.close, null)
                    .show()
            },
            onFavoriteClick = { character ->
                lifecycleScope.launch {
                    AppContainer.getFavoritosRepository(requireContext()).alternarFavorito(
                        idKey = "CHAR_${character.id}",
                        originalId = character.id,
                        tipo = "PERSONAJE",
                        nombre = character.name,
                        subtitulo = character.job ?: "Personaje de One Piece",
                        imagenUrl = character.fruit?.imageUrl,
                        detalleInfo = character.bountyLabel()
                    )
                }
            }
        )
        characterAdapter = adapter
        return adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlow(AppContainer.getFavoritosRepository(requireContext()).observarIds()) { ids ->
            characterAdapter?.updateFavoritos(ids)
        }
    }
}
