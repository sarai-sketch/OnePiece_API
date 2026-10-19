package com.practica_api.ui.sagas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practica_api.databinding.ItemSagaBinding
import com.practica_api.domain.model.Saga

class SagaAdapter : ListAdapter<Saga, SagaAdapter.ViewHolder>(SagaDiff) {

    inner class ViewHolder(private val binding: ItemSagaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Saga) = with(binding) {
            numberText.text = item.number
            titleText.text = item.title
            chaptersText.text = "Capítulos: ${item.chapters}"
            volumesText.text = "Tomos: ${item.volumes}"
            episodesText.text = "Episodios: ${item.episodes}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemSagaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}

private object SagaDiff : DiffUtil.ItemCallback<Saga>() {
    override fun areItemsTheSame(old: Saga, new: Saga) = old.id == new.id
    override fun areContentsTheSame(old: Saga, new: Saga) = old == new
}
