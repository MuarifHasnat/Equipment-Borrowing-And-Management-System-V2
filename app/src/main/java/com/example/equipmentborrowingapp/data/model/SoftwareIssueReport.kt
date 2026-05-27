package com.example.equipmentborrowingapp.data.model

data class SoftwareIssueReport(
    val id: String = "",
    val institutionId: String = "",
    val roomId: String = "",
    val computerId: String = "",
    val computerName: String = "",
    val computerImageUrl: String = "",
    val softwareName: String = "",

    val reportedByUserId: String = "",
    val reportedByUserName: String = "",
    val reportedByStudentId: String = "",
    val reportedByDepartment: String = "",

    val issueType: String = "",
    val description: String = "",

    // Status flow:
    // Open -> In Progress -> Solved
    // Extra: Rejected
    val status: String = "Open",
    val severity: String = "Medium",


    val assignedTo: String = "",
    val resolvedBy: String = "",
    val resolvedAt: Long = 0L,
    val adminComment: String = "",
    val studentFeedback: String = "",

    val timestamp: Long = System.currentTimeMillis(),
    val updatedAt: Long = 0L
)