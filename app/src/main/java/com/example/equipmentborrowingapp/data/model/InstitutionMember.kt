package com.example.equipmentborrowingapp.data.model

data class InstitutionMember(
    val id: String = "",
    val uid: String = "",
    val institutionId: String = "",
    val role: String = "student",
    val status: String = "Pending",
    val joinedAt: Long = System.currentTimeMillis()
)