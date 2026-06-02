package com.example.equipmentborrowingapp.data.model
private const val DEFAULT_SOFTWARE_ISSUE_STATUS = "Open"
private const val DEFAULT_SOFTWARE_ISSUE_SEVERITY = "Medium"
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

    val status: String = DEFAULT_SOFTWARE_ISSUE_STATUS,
    val severity: String = DEFAULT_SOFTWARE_ISSUE_SEVERITY,


    val assignedTo: String = "",
    val resolvedBy: String = "",
    val resolvedAt: Long = 0L,
    val adminComment: String = "",
    val studentFeedback: String = "",

    val timestamp: Long = System.currentTimeMillis(),
    val updatedAt: Long = 0L
)