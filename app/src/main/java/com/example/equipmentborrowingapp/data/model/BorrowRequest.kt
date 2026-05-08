package com.example.equipmentborrowingapp.data.model

data class BorrowRequest(
    val requestId: String = "",

    val institutionId: String = "",
    val roomId: String = "",

    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val studentId: String = "",
    val department: String = "",

    val equipmentId: String = "",
    val equipmentName: String = "",
    val equipmentCategory: String = "",
    val equipmentImageName: String = "",
    val equipmentImageUrl: String = "",

    val quantity: Int = 1,

    val purpose: String = "",
    val borrowDate: String = "",
    val dueDate: String = "",
    val returnedDate: String = "",

    val status: String = "Pending",

    val approvedBy: String = "",
    val issuedBy: String = "",
    val returnedTo: String = "",

    val requestTimestamp: Long = System.currentTimeMillis()
)