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
) : ListAdapter<DevilFruit, FruitAdapter.ViewHolder>(FruitDiff) {

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
