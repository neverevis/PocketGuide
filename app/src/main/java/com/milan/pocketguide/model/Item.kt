package com.milan.pocketguide.model

import java.io.Serializable

data class Item(
    val picture: Int,
    val title: String,
    val category: String,
    val address: String,
    val website: String,
    val telephone: String,
    val plusCode: String
) : Serializable