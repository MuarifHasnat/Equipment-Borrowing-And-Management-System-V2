package com.example.equipmentborrowingapp.data.model
private const val DEFAULT_ADMIN_REQUEST_STATUS = "Pending"
data class InstitutionAdminRequest(
    val id: String = "",
    val institutionId: String = "",
    val institutionName: String = "",
    val adminName: String = "",
    val adminEmail: String = "",
    val status: String = DEFAULT_ADMIN_REQUEST_STATUS,
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = 0L
)