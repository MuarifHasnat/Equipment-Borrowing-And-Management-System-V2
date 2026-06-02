package com.example.equipmentborrowingapp.data.model
private const val DEFAULT_REQUEST_STATUS = "Pending"
private const val DEFAULT_FINE_STATUS = "None"
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




    val approvedBy: String = "",
    val approvedAt: Long = 0L,

    val issuedBy: String = "",
    val issuedAt: Long = 0L,

    val returnedBy: String = "",
    val returnedAt: Long = 0L,

    val status: String = DEFAULT_REQUEST_STATUS,
    val returnedTo: String = "",

    val rejectedReason: String = "",
    val returnCondition: String = "",
    val adminNote: String = "",

    val fineAmount: Int = 0,
    val fineReason: String = "",
    val fineStatus: String = DEFAULT_FINE_STATUS,
    val penaltyUpdatedAt: Long = 0L,

    val requestTimestamp: Long = System.currentTimeMillis()
)