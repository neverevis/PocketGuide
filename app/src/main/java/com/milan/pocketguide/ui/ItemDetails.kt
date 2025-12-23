package com.milan.pocketguide.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.milan.pocketguide.R
import com.milan.pocketguide.databinding.ItemDetailsBinding
import com.milan.pocketguide.model.Item

class ItemDetails : AppCompatActivity() {
    private lateinit var binding: ItemDetailsBinding
    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        setupViews()
        setupListeners()
    }

    private fun loadData() {
        item = intent.getSerializableExtra("item", Item::class.java) as Item
    }

    private fun setupViews() {
        binding.tvTitle.text = item.title
        binding.tvCategory.text = item.category
        binding.tvAddress.text = item.address
        binding.tvTelephone.text = item.telephone
        binding.tvWebsite.text = item.website
        val uri = runCatching { Uri.parse(item.picture) }.getOrNull()
        if (uri?.scheme == "android.resource") {
            val segments = uri.pathSegments
            val resName = when {
                segments.size >= 2 && segments[0] == "drawable" -> segments[1]
                segments.isNotEmpty() -> segments.last()
                else -> item.picture.substringAfterLast('/').trim()
            }
            val resId = resources.getIdentifier(resName, "drawable", packageName)
            if (resId != 0) {
                binding.imgPicture.setImageResource(resId)
            } else {
                binding.imgPicture.setImageResource(R.drawable.ic_launcher_foreground)
            }
        } else {
            Glide.with(binding.imgPicture.context)
                .load(item.picture)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.imgPicture)
        }
    }

    private fun setupListeners() {
        binding.btnLigar.setOnClickListener {
            val telFormatted = "+55" + item.telephone.filter { it.isDigit() }
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telFormatted"))
            startActivity(intent)
        }

        binding.btnMap.setOnClickListener {
            val query = if (item.plusCode.isNotBlank()) {
                item.plusCode
            } else {
                item.address
            }
            val uri = Uri.parse("geo:0,0?q=" + Uri.encode(query))
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }
}