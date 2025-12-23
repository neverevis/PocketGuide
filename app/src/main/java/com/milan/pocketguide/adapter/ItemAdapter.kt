package com.milan.pocketguide.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.milan.pocketguide.R
import com.milan.pocketguide.databinding.ListItemBinding
import com.milan.pocketguide.model.Item

class ItemAdapter(
    private val onItemClick: (Item) -> Unit
) : ListAdapter<Item, ItemAdapter.ViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            val ctx = binding.imgPicture.context
            val pic = item.picture
            val uri = runCatching { Uri.parse(pic) }.getOrNull()

            if (uri?.scheme == "android.resource") {
                // carrega resource drawable direto
                val segments = uri.pathSegments
                val resName = when {
                    segments.size >= 2 && segments[0] == "drawable" -> segments[1]
                    segments.isNotEmpty() -> segments.last()
                    else -> pic.substringAfterLast('/').trim()
                }
                val resId = ctx.resources.getIdentifier(resName, "drawable", ctx.packageName)
                if (resId != 0) {
                    binding.imgPicture.setImageResource(resId)
                } else {
                    binding.imgPicture.setImageResource(R.drawable.ic_launcher_foreground)
                }
            } else {
                // carrega uri da galeria ou arquivo com glide
                Glide.with(ctx)
                    .load(pic)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.imgPicture)
            }
            binding.tvTitle.text = item.title
            binding.tvCategory.text = item.category
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}