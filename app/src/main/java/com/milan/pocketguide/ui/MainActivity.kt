package com.milan.pocketguide.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.milan.pocketguide.R
import com.milan.pocketguide.adapter.ItemAdapter
import com.milan.pocketguide.databinding.ActivityMainBinding
import com.milan.pocketguide.model.Item

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var items: List<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Força modo claro:
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // 🌙 Força modo escuro:
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        setupViews()
        setupListeners()
    }

    private fun loadData(){
        items = listOf(
            Item(
                R.drawable.panobianco,
                "Panobianco",
                getString(R.string.gym),
                "R. Maurício Onofre Cardilli, 81 - Vila Suconasa, Araraquara - SP, 14807-065",
                "panobiancoacademia.com.br",
                "(16)988557292",
                "5RVH+FC Vila Suconasa, Araraquara - SP"
            ),
            Item(
                R.drawable.luizao,
                "Luizão Lanches",
                getString(R.string.snack_bar),
                "Vila Melhado, Araraquara - SP, 14807-038",
                "panobiancoacademia.com.br",
                "(16)33228238",
                "5RRM+F2 Araraquara, São Paulo"
            ),
            Item(
                R.drawable.cardiesel,
                "Cardiesel",
                getString(R.string.mechanic),
                "R. Maurício Onofre Cardilli, 475 - Vila Suconasa, Araraquara - SP, 14807-065",
                "",
                "(16)997636759",
                "5RRJ+PP Vila Suconasa, Araraquara - SP"
            ),
            Item(
                R.drawable.tiba,
                "Supermercado Tiba",
                getString(R.string.supermarket),
                "Av. José Nogueira Neves, 490 - Vila Melhado, Araraquara - SP, 14807-034",
                "",
                "(16)33227114",
                "5RVJ+59 Vila Melhado, Araraquara - SP"
            ),
            Item(
                R.drawable.teatro,
                "Teatro da Arena",
                getString(R.string.theatre),
                "Av. Gil Martinez Perez, s/n - Vila Melhado, Araraquara - SP, 14807-038",
                "",
                "",
                "5RRM+85 Vila Melhado, Araraquara - SP"
            ),
            Item(
                R.drawable.cutrale,
                "Portal Cutrale",
                getString(R.string.food_industry),
                "Av. Padre José de Anchieta, 470 - Vila Furlan, Araraquara - SP",
                "",
                "",
                "5RWH+6Q Vila Furlan, Araraquara - SP"
            ),

        ).sortedBy { it.title }
    }

    private fun setupViews() {
        val adapter = ItemAdapter(this, items)
        binding.listViewItems.adapter = adapter
    }

    fun setupListeners(){
        binding.listViewItems.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, ItemDetails::class.java)
            intent.putExtra("item", items[position])
            startActivity(intent)
        }
    }
}