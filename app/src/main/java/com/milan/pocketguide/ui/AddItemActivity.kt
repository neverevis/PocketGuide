package com.milan.pocketguide.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.milan.pocketguide.databinding.ActivityAddItemBinding
import com.milan.pocketguide.model.Item
import com.milan.pocketguide.R
import com.milan.pocketguide.data.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddItemBinding
    private var imageUri: Uri? = null
    private lateinit var repository: ItemRepository

    private val pickImage = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            // obter permissão persistente de leitura
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUri = it
            Glide.with(binding.imgPreview.context)
                .load(it)
                .into(binding.imgPreview)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repository = ItemRepository.getInstance(applicationContext)

        binding.btnChooseImage.setOnClickListener {
            pickImage.launch(arrayOf("image/*"))
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val category = binding.etCategory.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val website = binding.etWebsite.text.toString().trim()
            val telephone = binding.etTelephone.text.toString().trim()
            val plusCode = binding.etPlusCode.text.toString().trim()

            if (title.isEmpty()) {
                binding.etTitle.error = "required"
                return@setOnClickListener
            }

            val picture = imageUri?.toString() ?: ""

            val item = Item(
                picture,
                title,
                category,
                address,
                website,
                telephone,
                plusCode
            )
            lifecycleScope.launch(Dispatchers.IO) {
                // inserir diretamente no banco de dados para manter a fonte única de verdade
                repository.insert(item)
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
