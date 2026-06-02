package com.example.equipmentborrowingapp.data.model
private const val DEFAULT_APP_USER_VERIFICATION_STATUS = "pending"
data class AppUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val institutionId: String = "",
    val verificationStatus: String = DEFAULT_APP_USER_VERIFICATION_STATUS,
    val studentId: String = "",
    val department: String = "",
    val semester: String = "",
    val phone: String = "",
    val profileImageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)