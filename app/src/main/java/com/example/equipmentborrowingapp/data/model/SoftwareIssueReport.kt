package com.example.equipmentborrowingapp.data.model

data class SoftwareIssueReport(
    val id: String = "",
    val institutionId: String = "",
    val roomId: String = "",
    val computerId: String = "",
    val computerName: String = "",
    val softwareName: String = "",
    val reportedByUserId: String = "",
    val reportedByUserName: String = "",
    val issueType: String = "",
    val description: String = "",
    val status: String = "Open",
    val severity: String = "Medium",
    val timestamp: Long = System.currentTimeMillis()
)