package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.ComputerSoftwareStatus
import com.example.equipmentborrowingapp.data.model.LabComputer
import com.example.equipmentborrowingapp.data.model.SoftwareIssueReport
import com.google.firebase.firestore.FirebaseFirestore


class LabComputerRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun addLabComputer(
        institutionId: String,
        roomId: String = "",
        pcName: String,
        labRoom: String,
        locationNote: String,
        ipAddress: String,
        status: String,
        remarks: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(false, "Institution not found")
            return
        }

        val docRef = firestore.collection("lab_computers").document()

        val computer = LabComputer(
            id = docRef.id,
            institutionId = institutionId,
            roomId = roomId,
            pcName = pcName.trim(),
            labRoom = labRoom.trim(),
            locationNote = locationNote.trim(),
            ipAddress = ipAddress.trim(),
            status = status.trim(),
            remarks = remarks.trim(),
            lastCheckedAt = System.currentTimeMillis()
        )

        docRef.set(computer)
            .addOnSuccessListener {
                onResult(true, "Lab computer added successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to add lab computer")
            }
    }

    fun getLabComputers(
        institutionId: String,
        onResult: (List<LabComputer>) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("lab_computers")
            .whereEqualTo("institutionId", institutionId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(LabComputer::class.java)
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun updateLabComputer(
        computer: LabComputer,
        onResult: (Boolean, String) -> Unit
    ) {
        if (computer.id.isBlank()) {
            onResult(false, "Lab computer not found")
            return
        }

        if (computer.institutionId.isBlank()) {
            onResult(false, "Institution not found")
            return
        }

        val updatedComputer = computer.copy(
            lastCheckedAt = System.currentTimeMillis()
        )

        firestore.collection("lab_computers")
            .document(computer.id)
            .set(updatedComputer)
            .addOnSuccessListener {
                onResult(true, "Lab computer updated successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update lab computer")
            }
    }

    fun deleteLabComputer(
        computerId: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (computerId.isBlank()) {
            onResult(false, "Invalid computer id")
            return
        }

        firestore.collection("computer_software_status")
            .whereEqualTo("computerId", computerId)
            .get()
            .addOnSuccessListener { softwareResult ->
                if (softwareResult.documents.isNotEmpty()) {
                    onResult(false, "Remove linked software records first")
                    return@addOnSuccessListener
                }

                firestore.collection("software_issue_reports")
                    .whereEqualTo("computerId", computerId)
                    .get()
                    .addOnSuccessListener { issueResult ->
                        val hasOpenIssues = issueResult.documents.any { doc ->
                            val status = doc.getString("status")?.trim().orEmpty()
                            !status.equals("Resolved", ignoreCase = true)
                        }

                        if (hasOpenIssues) {
                            onResult(false, "Cannot delete computer with unresolved issue reports")
                            return@addOnSuccessListener
                        }

                        firestore.collection("lab_computers")
                            .document(computerId)
                            .delete()
                            .addOnSuccessListener {
                                onResult(true, "Lab computer deleted successfully")
                            }
                            .addOnFailureListener { e ->
                                onResult(false, e.message ?: "Failed to delete lab computer")
                            }
                    }
                    .addOnFailureListener { e ->
                        onResult(false, e.message ?: "Failed to check related issue reports")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to check linked software records")
            }
    }

    fun addSoftwareStatus(
        institutionId: String,
        roomId: String = "",
        computerId: String,
        softwareName: String,
        version: String,
        installed: Boolean,
        launchesProperly: Boolean,
        compileWorks: Boolean,
        runWorks: Boolean,
        remarks: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(false, "Institution not found")
            return
        }

        val docRef = firestore.collection("computer_software_status").document()

        val softwareStatus = ComputerSoftwareStatus(
            id = docRef.id,
            institutionId = institutionId,
            roomId = roomId,
            computerId = computerId,
            softwareName = softwareName.trim(),
            version = version.trim(),
            installed = installed,
            launchesProperly = launchesProperly,
            compileWorks = compileWorks,
            runWorks = runWorks,
            remarks = remarks.trim(),
            checkedAt = System.currentTimeMillis()
        )

        docRef.set(softwareStatus)
            .addOnSuccessListener {
                onResult(true, "Software status added successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to add software status")
            }
    }

    fun getSoftwareStatusForComputer(
        institutionId: String,
        computerId: String,
        onResult: (List<ComputerSoftwareStatus>) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("computer_software_status")
            .whereEqualTo("institutionId", institutionId)
            .whereEqualTo("computerId", computerId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(ComputerSoftwareStatus::class.java)
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun submitSoftwareIssueReport(
        institutionId: String,
        roomId: String = "",
        computerId: String,
        computerName: String,
        softwareName: String,
        reportedByUserId: String,
        reportedByUserName: String,
        issueType: String,
        description: String,
        severity: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(false, "Institution not found")
            return
        }

        val docRef = firestore.collection("software_issue_reports").document()

        val report = SoftwareIssueReport(
            id = docRef.id,
            institutionId = institutionId,
            roomId = roomId,
            computerId = computerId,
            computerName = computerName,
            softwareName = softwareName.trim(),
            reportedByUserId = reportedByUserId,
            reportedByUserName = reportedByUserName,
            issueType = issueType.trim(),
            description = description.trim(),
            severity = severity.trim(),
            status = "Open",
            timestamp = System.currentTimeMillis()
        )

        docRef.set(report)
            .addOnSuccessListener {
                onResult(true, "Issue reported successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to submit issue report")
            }
    }
    fun getSoftwareIssueReports(
        institutionId: String,
        onResult: (List<SoftwareIssueReport>) -> Unit
    ) {
        val cleanInstitutionId = institutionId.trim()

        if (cleanInstitutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("software_issue_reports")
            .whereEqualTo("institutionId", cleanInstitutionId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(SoftwareIssueReport::class.java)
                }.sortedByDescending { report ->
                    report.timestamp
                }

                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
    fun getStudentSoftwareIssueReports(
        institutionId: String,
        userId: String,
        onResult: (List<SoftwareIssueReport>) -> Unit
    ) {
        val cleanUserId = userId.trim()

        if (cleanUserId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("software_issue_reports")
            .whereEqualTo("reportedByUserId", cleanUserId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(SoftwareIssueReport::class.java)
                }.filter { report ->
                    institutionId.isBlank() || report.institutionId == institutionId.trim()
                }.sortedByDescending { report ->
                    report.timestamp
                }

                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
    fun updateIssueReportStatus(
        reportId: String,
        status: String,
        adminComment: String = "",
        handledBy: String = "",
        onResult: (Boolean, String) -> Unit
    ) {
        if (reportId.isBlank()) {
            onResult(false, "Invalid report id")
            return
        }

        if (status.isBlank()) {
            onResult(false, "Status is required")
            return
        }

        val normalizedStatus = status.trim()
        val now = System.currentTimeMillis()

        val updateMap = mutableMapOf<String, Any>(
            "status" to normalizedStatus,
            "updatedAt" to now
        )

        if (adminComment.isNotBlank()) {
            updateMap["adminComment"] = adminComment.trim()
        }

        if (handledBy.isNotBlank()) {
            updateMap["assignedTo"] = handledBy
        }

        if (
            normalizedStatus.equals("Solved", ignoreCase = true) ||
            normalizedStatus.equals("Resolved", ignoreCase = true)
        ) {
            updateMap["resolvedBy"] = handledBy
            updateMap["resolvedAt"] = now
            updateMap["status"] = "Solved"
        }

        firestore.collection("software_issue_reports")
            .document(reportId)
            .update(updateMap)
            .addOnSuccessListener {
                onResult(true, "Issue report updated successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update issue report")
            }
    }
    fun addStudentFeedbackToIssueReport(
        reportId: String,
        studentFeedback: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (reportId.isBlank()) {
            onResult(false, "Invalid report id")
            return
        }

        if (studentFeedback.isBlank()) {
            onResult(false, "Feedback is required")
            return
        }

        val reportRef = firestore.collection("software_issue_reports")
            .document(reportId)

        reportRef.get()
            .addOnSuccessListener { document ->
                val report = document.toObject(SoftwareIssueReport::class.java)

                if (report == null) {
                    onResult(false, "Issue report not found")
                    return@addOnSuccessListener
                }

                if (!report.status.equals("Solved", ignoreCase = true)) {
                    onResult(false, "Feedback can be submitted only after the issue is solved")
                    return@addOnSuccessListener
                }

                reportRef.update(
                    mapOf(
                        "studentFeedback" to studentFeedback.trim(),
                        "updatedAt" to System.currentTimeMillis()
                    )
                )
                    .addOnSuccessListener {
                        onResult(true, "Feedback submitted successfully")
                    }
                    .addOnFailureListener { e ->
                        onResult(false, e.message ?: "Failed to submit feedback")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to check issue report")
            }
    }
    fun updateSoftwareStatus(
        softwareStatus: ComputerSoftwareStatus,
        onResult: (Boolean, String) -> Unit
    ) {
        if (softwareStatus.id.isBlank()) {
            onResult(false, "Software status not found")
            return
        }

        if (softwareStatus.institutionId.isBlank()) {
            onResult(false, "Institution not found")
            return
        }

        if (softwareStatus.computerId.isBlank()) {
            onResult(false, "Computer not found")
            return
        }

        if (softwareStatus.softwareName.isBlank()) {
            onResult(false, "Software name is required")
            return
        }

        val updatedStatus = softwareStatus.copy(
            softwareName = softwareStatus.softwareName.trim(),
            version = softwareStatus.version.trim(),
            remarks = softwareStatus.remarks.trim(),
            checkedAt = System.currentTimeMillis()
        )

        firestore.collection("computer_software_status")
            .document(softwareStatus.id)
            .set(updatedStatus)
            .addOnSuccessListener {
                onResult(true, "Software status updated successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update software status")
            }
    }

    fun deleteSoftwareStatus(
        softwareStatusId: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (softwareStatusId.isBlank()) {
            onResult(false, "Invalid software status id")
            return
        }

        firestore.collection("computer_software_status")
            .document(softwareStatusId)
            .delete()
            .addOnSuccessListener {
                onResult(true, "Software status deleted successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to delete software status")
            }
    }
}