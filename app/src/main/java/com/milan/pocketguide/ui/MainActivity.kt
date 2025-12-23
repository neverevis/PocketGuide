package com.milan.pocketguide.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first
import androidx.recyclerview.widget.LinearLayoutManager
import com.milan.pocketguide.R
import com.milan.pocketguide.adapter.ItemAdapter
import com.milan.pocketguide.databinding.ActivityMainBinding
import com.milan.pocketguide.data.ItemRepository
import com.milan.pocketguide.model.Item
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var items: List<Item> = emptyList()
    private lateinit var adapter: ItemAdapter
    private lateinit var repository: ItemRepository
    private val defaultItems: List<Item> by lazy {
        listOf(
            Item(
                "android.resource://" + packageName + "/drawable/panobianco",
                "Panobianco",
                getString(R.string.gym),
                "R. Maurício Onofre Cardilli, 81 - Vila Suconasa, Araraquara - SP, 14807-065",
                "panobiancoacademia.com.br",
                "(16)988557292",
                "5RVH+FC Vila Suconasa, Araraquara - SP"
            ),
            Item(
                "android.resource://" + packageName + "/drawable/luizao",
                "Luizão Lanches",
                getString(R.string.snack_bar),
                "Vila Melhado, Araraquara - SP, 14807-038",
                "panobiancoacademia.com.br",
                "(16)33228238",
                "5RRM+F2 Araraquara, São Paulo"
            ),
            Item(
                "android.resource://" + packageName + "/drawable/cardiesel",
                "Cardiesel",
                getString(R.string.mechanic),
                "R. Maurício Onofre Cardilli, 475 - Vila Suconasa, Araraquara - SP, 14807-065",
                "",
                "(16)997636759",
                "5RRJ+PP Vila Suconasa, Araraquara - SP"
            ),
            Item(
                "android.resource://" + packageName + "/drawable/tiba",
                "Supermercado Tiba",
                getString(R.string.supermarket),
                "Av. José Nogueira Neves, 490 - Vila Melhado, Araraquara - SP, 14807-034",
                "",
                "(16)33227114",
                "5RVJ+59 Vila Melhado, Araraquara - SP"
            ),
            Item(
                "android.resource://" + packageName + "/drawable/teatro",
                "Teatro da Arena",
                getString(R.string.theatre),
                "Av. Gil Martinez Perez, s/n - Vila Melhado, Araraquara - SP, 14807-038",
                "",
                "",
                "5RRM+85 Vila Melhado, Araraquara - SP"
            ),
            Item(
                "android.resource://" + packageName + "/drawable/cutrale",
                "Portal Cutrale",
                getString(R.string.food_industry),
                "Av. Padre José de Anchieta, 470 - Vila Furlan, Araraquara - SP",
                "",
                "",
                "5RWH+6Q Vila Furlan, Araraquara - SP"
            ),
        ).sortedBy { it.title }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // força modo claro:
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // força modo escuro:
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = ItemRepository.getInstance(applicationContext)
        setupViews()
        setupListeners()
        setupAddItemLauncher()

        seedDefaultsOnce()

        observeItems()
    }

    

    private fun setupAddItemLauncher() {
        val launcher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // cadastro salvou diretamente no banco; a lista será atualizada via flow
            }
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            launcher.launch(intent)
        }
    }

    private fun observeItems() {
        lifecycleScope.launch {
            repository.getAll().collect { list ->
                items = list.sortedBy { it.title }
                val query = binding.etSearch.text?.toString().orEmpty()
                if (query.isBlank()) {
                    adapter.submitList(items)
                } else {
                    filterList(query)
                }
            }
        }
    }

    private fun seedDefaultsOnce() {
        val prefs = getSharedPreferences("pocketguide_prefs", MODE_PRIVATE)
        val seeded = prefs.getBoolean("seed_done", false)
        if (seeded) return
        lifecycleScope.launch(Dispatchers.IO) {
            val current = repository.getAll().first()
            if (current.isEmpty()) {
                repository.insertAll(defaultItems)
            }
            prefs.edit().putBoolean("seed_done", true).apply()
        }
    }

    private fun setupViews() {
        adapter = ItemAdapter { item ->
            val intent = Intent(this, ItemDetails::class.java)
            intent.putExtra("item", item)
            startActivity(intent)
        }

        binding.rvItems.layoutManager = LinearLayoutManager(this)
        binding.rvItems.setHasFixedSize(true)
        binding.rvItems.adapter = adapter
        adapter.submitList(items)
    }

    fun setupListeners(){
        binding.etSearch.addTextChangedListener(onTextChanged = { text, _, _, _ ->
            val query = text?.toString() ?: ""
            filterList(query)
        })
    }

    private fun filterList(query: String) {
        if (query.isBlank()) {
            adapter.submitList(items)
        } else {
            val filtered = items.filter { it.title.contains(query, ignoreCase = true) }
            adapter.submitList(filtered)
        }
    }
}