package com.milan.pocketguide.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.milan.pocketguide.model.Item

@Entity(tableName = "items")
data class ItemEntity(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	val picture: String,
	val title: String,
	val category: String,
	val address: String,
	val website: String,
	val telephone: String,
	val plusCode: String
)

fun ItemEntity.toModel(): Item = Item(
	picture,
	title,
	category,
	address,
	website,
	telephone,
	plusCode
)

fun Item.toEntity(): ItemEntity = ItemEntity(
	picture = this.picture,
	title = this.title,
	category = this.category,
	address = this.address,
	website = this.website,
	telephone = this.telephone,
	plusCode = this.plusCode
)
