package com.example.equipmentborrowingapp.data.model

data class AppNotification(
    val id: String = "",
    val institutionId: String = "",
    val userId: String = "",
    val role: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "info",
    val read: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)