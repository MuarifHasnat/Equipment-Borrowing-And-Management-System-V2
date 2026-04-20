package com.example.equipmentborrowingapp.data.model

data class BorrowRequest(
    val requestId: String = "",
    val userId: String = "",
    val userName: String = "",
    val equipmentId: String = "",
    val equipmentName: String = "",
    val equipmentCategory: String = "",
    val equipmentImageName: String = "",
    val equipmentImageUrl: String = "",
    val quantity: Int = 1,
    val borrowDate: String = "",
    val dueDate: String = "",
    val returnedDate: String = "",
    val status: String = "Pending",
    val requestTimestamp: Long = System.currentTimeMillis()
)