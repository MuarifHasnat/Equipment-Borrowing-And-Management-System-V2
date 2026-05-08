package com.example.equipmentborrowingapp.data.model

data class Room(
    val id: String = "",
    val institutionId: String = "",
    val name: String = "",          // Room 401 / Software Lab 1
    val building: String = "",
    val floor: String = "",
    val roomType: String = "Lab",   // Lab / Classroom / Store / Workshop
    val department: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)