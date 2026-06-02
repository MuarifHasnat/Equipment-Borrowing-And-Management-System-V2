package com.example.equipmentborrowingapp.data.model
private const val DEFAULT_USER_ROLE = "student"
private const val DEFAULT_USER_VERIFICATION_STATUS = "Pending"
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",

    val institutionId: String = "",
    val studentId: String = "",
    val employeeId: String = "",

    val department: String = "",
    val semester: String = "",
    val batch: String = "",

    val role: String = DEFAULT_USER_ROLE,


    val verificationStatus: String = DEFAULT_USER_VERIFICATION_STATUS,

    val profileImageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)