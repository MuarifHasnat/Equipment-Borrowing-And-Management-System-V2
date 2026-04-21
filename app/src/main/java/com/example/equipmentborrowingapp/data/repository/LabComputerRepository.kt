package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.ComputerSoftwareStatus
import com.example.equipmentborrowingapp.data.model.LabComputer
import com.example.equipmentborrowingapp.data.model.SoftwareIssueReport
import com.google.firebase.firestore.FirebaseFirestore

class LabComputerRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun addLabComputer(
        pcName: String,
        labRoom: String,
        locationNote: String,
        ipAddress: String,
        status: String,
        remarks: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (pcName.isBlank()) {
            onResult(false, "PC name is required")
            return
        }

        if (labRoom.isBlank()) {
            onResult(false, "Lab room is required")
            return
        }

        val docRef = firestore.collection("lab_computers").document()

        val computer = LabComputer(
            id = docRef.id,
            pcName = pcName.trim(),
            labRoom = labRoom.trim(),
            locationNote = locationNote.trim(),
            ipAddress = ipAddress.trim(),
            status = status.trim().ifBlank { "Active" },
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
        onResult: (List<LabComputer>) -> Unit
    ) {
        firestore.collection("lab_computers")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { it.toObject(LabComputer::class.java) }
                    .sortedBy { it.pcName.lowercase() }
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
            onResult(false, "Invalid computer id")
            return
        }

        if (computer.pcName.isBlank()) {
            onResult(false, "PC name is required")
            return
        }

        if (computer.labRoom.isBlank()) {
            onResult(false, "Lab room is required")
            return
        }

        firestore.collection("lab_computers")
            .document(computer.id)
            .set(
                computer.copy(
                    pcName = computer.pcName.trim(),
                    labRoom = computer.labRoom.trim(),
                    locationNote = computer.locationNote.trim(),
                    ipAddress = computer.ipAddress.trim(),
                    status = computer.status.trim().ifBlank { "Active" },
                    remarks = computer.remarks.trim(),
                    lastCheckedAt = System.currentTimeMillis()
                )
            )
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
        if (computerId.isBlank()) {
            onResult(false, "Computer id is required")
            return
        }

        if (softwareName.isBlank()) {
            onResult(false, "Software name is required")
            return
        }

        val docRef = firestore.collection("computer_software_status").document()

        val item = ComputerSoftwareStatus(
            id = docRef.id,
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

        docRef.set(item)
            .addOnSuccessListener {
                onResult(true, "Software status added successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to add software status")
            }
    }

    fun getSoftwareStatusForComputer(
        computerId: String,
        onResult: (List<ComputerSoftwareStatus>) -> Unit
    ) {
        firestore.collection("computer_software_status")
            .whereEqualTo("computerId", computerId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(ComputerSoftwareStatus::class.java)
                }.sortedBy { it.softwareName.lowercase() }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun submitSoftwareIssueReport(
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
        if (computerId.isBlank()) {
            onResult(false, "Computer id is required")
            return
        }

        if (computerName.isBlank()) {
            onResult(false, "Computer name is required")
            return
        }

        if (softwareName.isBlank()) {
            onResult(false, "Software name is required")
            return
        }

        if (reportedByUserId.isBlank() || reportedByUserName.isBlank()) {
            onResult(false, "Reporter information is required")
            return
        }

        if (issueType.isBlank()) {
            onResult(false, "Issue type is required")
            return
        }

        if (description.isBlank()) {
            onResult(false, "Issue description is required")
            return
        }

        val docRef = firestore.collection("software_issue_reports").document()

        val report = SoftwareIssueReport(
            id = docRef.id,
            computerId = computerId,
            computerName = computerName.trim(),
            softwareName = softwareName.trim(),
            reportedByUserId = reportedByUserId.trim(),
            reportedByUserName = reportedByUserName.trim(),
            issueType = issueType.trim(),
            description = description.trim(),
            status = "Open",
            severity = severity.trim().ifBlank { "Medium" },
            timestamp = System.currentTimeMillis()
        )

        docRef.set(report)
            .addOnSuccessListener {
                onResult(true, "Issue report submitted successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to submit issue report")
            }
    }

    fun getSoftwareIssueReports(
        onResult: (List<SoftwareIssueReport>) -> Unit
    ) {
        firestore.collection("software_issue_reports")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(SoftwareIssueReport::class.java)
                }.sortedByDescending { it.timestamp }
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