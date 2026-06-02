package com.example.equipmentborrowingapp.data.model
private const val DEFAULT_SOFTWARE_INSTALL_STATUS = "Pending"
data class SoftwareInstallRequest(
    val id: String = "",
    val institutionId: String = "",

    val computerId: String = "",
    val computerName: String = "",
    val computerImageUrl: String = "",

    val softwareName: String = "",
    val version: String = "",
    val softwareLogoUrl: String = "",

    val requestedByUserId: String = "",
    val requestedByUserName: String = "",
    val requestedByStudentId: String = "",
    val requestedByDepartment: String = "",
    val requestedByEmail: String = "",

    val reason: String = "",

    // Pending, Approved, Rejected, Installed
    val status: String = DEFAULT_SOFTWARE_INSTALL_STATUS,

    val adminMessage: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)