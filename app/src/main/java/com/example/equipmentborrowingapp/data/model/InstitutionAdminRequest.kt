package com.example.equipmentborrowingapp.data.model

data class InstitutionAdminRequest(
    val id: String = "",
    val institutionId: String = "",
    val institutionName: String = "",
    val adminName: String = "",
    val adminEmail: String = "",
    val status: String = "Pending", // Pending / Approved / Rejected / Created
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = 0L
)