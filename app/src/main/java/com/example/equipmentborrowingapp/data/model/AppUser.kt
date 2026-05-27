package com.example.equipmentborrowingapp.data.model

data class AppUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val institutionId: String = "",
    val verificationStatus: String = "pending",
    val studentId: String = "",
    val department: String = "",
    val semester: String = "",
    val phone: String = "",
    val profileImageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)