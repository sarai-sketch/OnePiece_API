package com.practica_api.ui.sagas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practica_api.R
import com.practica_api.databinding.ItemSagaBinding
import com.practica_api.domain.model.Saga

class SagaAdapter(
    private val onClick: ((Saga) -> Unit)? = null,
    private val onFavoriteClick: ((Saga) -> Unit)? = null,
    private var favoritosIds: Set<String> = emptySet(),
) : ListAdapter<Saga, SagaAdapter.ViewHolder>(SagaDiff) {

    fun updateFavoritos(nuevosIds: Set<String>) {
        favoritosIds = nuevosIds
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemSagaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Saga) = with(binding) {
            numberText.text = item.number
            titleText.text = item.title
            chaptersText.text = "Capítulos: ${item.chapters}"
            volumesText.text = "Tomos: ${item.volumes}"
            episodesText.text = "Episodios: ${item.episodes}"

            val isFav = favoritosIds.contains("SAGA_${item.id}")
            btnFavorite.setImageResource(
                if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            btnFavorite.setOnClickListener { onFavoriteClick?.invoke(item) }

            root.setOnClickListener { onClick?.invoke(item) }
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
