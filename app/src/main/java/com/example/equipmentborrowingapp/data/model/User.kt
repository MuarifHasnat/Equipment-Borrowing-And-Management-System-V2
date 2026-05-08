package com.example.equipmentborrowingapp.data.model

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

    val role: String = "student",
    // student / lab_admin / institution_admin / super_admin

    val verificationStatus: String = "Pending",
    // Pending / Verified / Rejected

    val profileImageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)