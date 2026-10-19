package com.practica_api.ui.sagas

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practica_api.R
import com.practica_api.di.AppContainer
import com.practica_api.domain.model.Saga
import com.practica_api.ui.common.BaseListFragment
import com.practica_api.ui.common.ListViewModel
import com.practica_api.ui.common.ListViewModelFactory

class SagasFragment : BaseListFragment<Saga>() {

    override val searchHint = R.string.search_sagas

    override val viewModel by viewModels<ListViewModel<Saga>> {
        ListViewModelFactory(
            fetch = { refresh -> AppContainer.onePieceRepository.getSagas(refresh) },
            matches = { item, query -> item.title.contains(query, ignoreCase = true) },
        )
    }

    override fun createAdapter(): ListAdapter<Saga, out RecyclerView.ViewHolder> = SagaAdapter()
}
