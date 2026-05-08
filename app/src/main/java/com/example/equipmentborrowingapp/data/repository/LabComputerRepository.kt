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
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("software_issue_reports")
            .whereEqualTo("institutionId", institutionId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(SoftwareIssueReport::class.java)
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun updateIssueReportStatus(
        reportId: String,
        newStatus: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (reportId.isBlank()) {
            onResult(false, "Invalid report id")
            return
        }

        if (newStatus.isBlank()) {
            onResult(false, "Status is required")
            return
        }

        firestore.collection("software_issue_reports")
            .document(reportId)
            .update("status", newStatus.trim())
            .addOnSuccessListener {
                onResult(true, "Issue report status updated successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update issue report status")
            }
    }
}