package com.example.equipmentborrowingapp.data.model

data class SoftwareIssueReport(
    val id: String = "",
    val computerId: String = "",
    val computerName: String = "",
    val softwareName: String = "",
    val reportedByUserId: String = "",
    val reportedByUserName: String = "",
    val issueType: String = "", // Not Installed / Not Opening / Compile Error / Runtime Error / Other
    val description: String = "",
    val status: String = "Open", // Open / In Progress / Resolved
    val severity: String = "Medium", // Low / Medium / High
    val timestamp: Long = System.currentTimeMillis()
)