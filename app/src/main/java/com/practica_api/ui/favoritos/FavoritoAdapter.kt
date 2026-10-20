package com.practica_api.ui.favoritos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.practica_api.R
import com.practica_api.data.local.FavoritoEntity
import com.practica_api.databinding.ItemCharacterBinding

class FavoritoAdapter(
    private val onFavoriteClick: (FavoritoEntity) -> Unit
) : ListAdapter<FavoritoEntity, FavoritoAdapter.ViewHolder>(FavoritoDiff) {

    inner class ViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FavoritoEntity) = with(binding) {
            avatar.text = item.nombre.firstOrNull()?.uppercase() ?: "F"
            nameText.text = item.nombre
            subtitleText.text = item.subtitulo
            bountyText.text = item.detalleInfo ?: ""
            bountyText.isVisible = !item.detalleInfo.isNullOrBlank()

            statusText.text = item.tipo
            statusText.isVisible = true

            btnFavorite.setImageResource(R.drawable.ic_favorite)
            btnFavorite.setOnClickListener { onFavoriteClick(item) }

            val imageUrl = item.imagenUrl
            fruitImage.isVisible = !imageUrl.isNullOrBlank()
            if (!imageUrl.isNullOrBlank()) {
                fruitImage.load(imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_fruit)
                    error(R.drawable.ic_logo)
                }
            } else {
                fruitImage.setImageDrawable(null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private object FavoritoDiff : DiffUtil.ItemCallback<FavoritoEntity>() {
    override fun areItemsTheSame(old: FavoritoEntity, new: FavoritoEntity) = old.idKey == new.idKey
    override fun areContentsTheSame(old: FavoritoEntity, new: FavoritoEntity) = old == new
}
