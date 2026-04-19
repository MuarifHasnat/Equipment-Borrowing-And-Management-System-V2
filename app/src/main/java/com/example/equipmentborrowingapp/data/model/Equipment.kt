package com.example.equipmentborrowingapp.data.model

data class Equipment(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val condition: String = "",
    val totalQuantity: Int = 0,
    val availableQuantity: Int = 0,
    val isBorrowable: Boolean = true,
    val imageName: String = "",
    val imageUrl: String = ""
)