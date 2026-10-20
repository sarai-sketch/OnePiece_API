package com.practica_api.ui.fruits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.practica_api.R
import com.practica_api.databinding.ItemFruitBinding
import com.practica_api.domain.model.DevilFruit

class FruitAdapter(
    private val onClick: (DevilFruit) -> Unit,
    private val onFavoriteClick: ((DevilFruit) -> Unit)? = null,
    private var favoritosIds: Set<String> = emptySet(),
) : ListAdapter<DevilFruit, FruitAdapter.ViewHolder>(FruitDiff) {

    fun updateFavoritos(nuevosIds: Set<String>) {
        favoritosIds = nuevosIds
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemFruitBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DevilFruit) = with(binding) {
            image.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_fruit)
                error(R.drawable.ic_fruit)
                fallback(R.drawable.ic_fruit)
            }
            nameText.text = item.name
            romanText.text = item.romanName.orEmpty()
            typeText.text = item.type
            descriptionText.text = item.description ?: "Sin descripción."

            val isFav = favoritosIds.contains("FRUIT_${item.id}")
            btnFavorite.setImageResource(
                if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            btnFavorite.setOnClickListener { onFavoriteClick?.invoke(item) }

            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemFruitBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}

private object FruitDiff : DiffUtil.ItemCallback<DevilFruit>() {
    override fun areItemsTheSame(old: DevilFruit, new: DevilFruit) = old.id == new.id
    override fun areContentsTheSame(old: DevilFruit, new: DevilFruit) = old == new
}
