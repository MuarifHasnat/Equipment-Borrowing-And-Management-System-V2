package com.example.equipmentborrowingapp.data.model

data class Institution(
    val id: String = "",
    val name: String = "",
    val shortName: String = "",
    val emailDomain: String = "",      // example: aiub.edu
    val type: String = "university",   // university / college / institute
    val status: String = "Pending",    // Pending / Approved / Rejected
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)