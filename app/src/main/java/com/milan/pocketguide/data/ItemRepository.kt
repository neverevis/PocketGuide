package com.milan.pocketguide.data

// repositório que encapsula acesso ao room
import android.content.Context
import com.milan.pocketguide.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemRepository private constructor(private val db: AppDatabase) {
	private val dao = db.itemDao()

	fun getAll(): Flow<List<Item>> = dao.getAll().map { entities ->
		entities.map { it.toModel() }
	}

	suspend fun insert(item: Item) {
		dao.insert(item.toEntity())
	}

	suspend fun insertAll(items: List<Item>) {
		dao.insertAll(items.map { it.toEntity() })
	}

	companion object {
		@Volatile
		private var INSTANCE: ItemRepository? = null

		fun getInstance(context: Context): ItemRepository {
			return INSTANCE ?: synchronized(this) {
				val repo = ItemRepository(AppDatabase.getInstance(context))
				INSTANCE = repo
				repo
			}
		}
	}
}
