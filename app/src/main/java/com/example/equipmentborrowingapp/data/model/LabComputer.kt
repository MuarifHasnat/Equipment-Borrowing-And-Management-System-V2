package com.example.equipmentborrowingapp.data.model

data class LabComputer(
    val id: String = "",
    val pcName: String = "",
    val labRoom: String = "",
    val locationNote: String = "",
    val ipAddress: String = "",
    val status: String = "Active", // Active / Problematic / Maintenance
    val remarks: String = "",
    val lastCheckedAt: Long = System.currentTimeMillis()
)