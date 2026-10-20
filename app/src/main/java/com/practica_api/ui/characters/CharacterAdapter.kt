package com.practica_api.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.practica_api.R
import com.practica_api.databinding.ItemCharacterBinding
import com.practica_api.domain.model.OnePieceCharacter

class CharacterAdapter(
    private val onClick: (OnePieceCharacter) -> Unit,
    private val onFavoriteClick: ((OnePieceCharacter) -> Unit)? = null,
    private var favoritosIds: Set<String> = emptySet(),
) : ListAdapter<OnePieceCharacter, CharacterAdapter.ViewHolder>(CharacterDiff) {

    fun updateFavoritos(nuevosIds: Set<String>) {
        favoritosIds = nuevosIds
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OnePieceCharacter) = with(binding) {
            avatar.text = item.name.firstOrNull()?.uppercase() ?: "P"
            nameText.text = item.name
            subtitleText.text = listOfNotNull(item.job, item.crew?.name)
                .joinToString(" · ")
                .ifEmpty { "Sin información de cargo" }
            bountyText.text = item.bountyLabel() ?: "Sin recompensa registrada"
            statusText.text = item.statusLabel()

            val isFav = favoritosIds.contains("CHAR_${item.id}")
            btnFavorite.setImageResource(
                if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            btnFavorite.setOnClickListener { onFavoriteClick?.invoke(item) }

            val imageUrl = item.fruit?.imageUrl
            fruitImage.isVisible = imageUrl != null
            if (imageUrl != null) {
                fruitImage.load(imageUrl) {
                    crossfade(true)
                    listener(onError = { _, _ -> fruitImage.isVisible = false })
                }
            } else {
                fruitImage.setImageDrawable(null)
            }
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}

private object CharacterDiff : DiffUtil.ItemCallback<OnePieceCharacter>() {
    override fun areItemsTheSame(old: OnePieceCharacter, new: OnePieceCharacter) = old.id == new.id
    override fun areContentsTheSame(old: OnePieceCharacter, new: OnePieceCharacter) = old == new
}
