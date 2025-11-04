package com.milan.pocketguide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.milan.pocketguide.R
import com.milan.pocketguide.databinding.ListItemBinding
import com.milan.pocketguide.model.Item

class ItemAdapter(
    private val context: Context,
    private val lista: List<Item>
) : ArrayAdapter<Item>(context, 0, lista) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ListItemBinding
        val itemView: View

        if (convertView == null) {
            binding = ListItemBinding.inflate(LayoutInflater.from(context), parent, false)
            itemView = binding.root
            itemView.tag = binding
        } else {
            itemView = convertView
            binding = itemView.tag as ListItemBinding
        }

        val contato = lista[position]

        binding.imgPicture.setImageResource(contato.picture)
        binding.tvTitle.text = contato.title
        binding.tvCategory.text = contato.category

        return itemView
    }

}