package com.example.equipmentborrowingapp.data.model

data class AppNotification(
    val id: String = "",
    val userId: String = "",
    val role: String = "", // student / admin / all
    val title: String = "",
    val message: String = "",
    val type: String = "info", // info / success / warning / error
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)