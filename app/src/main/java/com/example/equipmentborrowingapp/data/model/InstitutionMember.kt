package com.example.equipmentborrowingapp.data.model
private const val DEFAULT_MEMBER_ROLE = "student"
private const val DEFAULT_MEMBER_STATUS = "Pending"
data class InstitutionMember(
    val id: String = "",
    val uid: String = "",
    val institutionId: String = "",
    val role: String = DEFAULT_MEMBER_ROLE,
    val status: String = DEFAULT_MEMBER_STATUS,
    val joinedAt: Long = System.currentTimeMillis()
)