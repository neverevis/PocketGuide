package com.milan.pocketguide.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
	@Query("SELECT * FROM items ORDER BY title")
	fun getAll(): Flow<List<ItemEntity>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(item: ItemEntity): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAll(items: List<ItemEntity>)
}
