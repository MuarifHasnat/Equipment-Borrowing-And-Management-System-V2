package com.example.equipmentborrowingapp.data.model
private const val DEFAULT_INSTITUTION_TYPE = "university"
private const val DEFAULT_INSTITUTION_STATUS = "Pending"
data class Institution(
    val id: String = "",
    val name: String = "",
    val shortName: String = "",
    val emailDomain: String = "",
    val type: String = DEFAULT_INSTITUTION_TYPE,
    val status: String = DEFAULT_INSTITUTION_STATUS,
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)