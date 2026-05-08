package com.example.equipmentborrowingapp.data.model

data class Room(
    val id: String = "",
    val institutionId: String = "",
    val name: String = "",
    val building: String = "",
    val floor: String = "",
    val roomType: String = "Lab",
    val department: String = "",
    val active: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)