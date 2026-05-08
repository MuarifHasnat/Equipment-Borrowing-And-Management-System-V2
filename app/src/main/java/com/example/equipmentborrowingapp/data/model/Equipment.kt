package com.example.equipmentborrowingapp.data.model
data class Equipment(
    val id: String = "",

    val institutionId: String = "",
    val roomId: String = "",

    val name: String = "",
    val description: String = "",
    val category: String = "",
    val condition: String = "",

    val totalQuantity: Int = 0,
    val availableQuantity: Int = 0,

    val isBorrowable: Boolean = true,
    val borrowType: String = "OutsideLab",

    val assetTag: String = "",
    val serialNumber: String = "",
    val imageName: String = "",
    val imageUrl: String = "",

    val addedBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)