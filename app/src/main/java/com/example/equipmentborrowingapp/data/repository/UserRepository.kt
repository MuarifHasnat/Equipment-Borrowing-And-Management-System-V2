package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.AppUser
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private fun normalizeStatus(status: String): String {
        return status.trim().lowercase()
    }

    private fun isValidStudentStatus(status: String): Boolean {
        return normalizeStatus(status) in listOf(
            "pending",
            "verified",
            "rejected",
            "suspended"
        )
    }

    private fun statusMessage(status: String): String {
        return when (normalizeStatus(status)) {
            "pending" -> "Student status changed to pending"
            "verified" -> "Student verified successfully"
            "rejected" -> "Student rejected successfully"
            "suspended" -> "Student suspended successfully"
            else -> "Student status updated successfully"
        }
    }

    fun getAllStudents(
        institutionId: String,
        onResult: (List<AppUser>) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("users")
            .whereEqualTo("institutionId", institutionId)
            .whereEqualTo("role", "student")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(AppUser::class.java)?.copy(
                        uid = document.id,
                        verificationStatus = normalizeStatus(
                            document.getString("verificationStatus") ?: ""
                        ).ifBlank { "pending" }
                    )
                }

                onResult(
                    list.sortedWith(
                        compareBy<AppUser> { it.verificationStatus }
                            .thenBy { it.name.lowercase() }
                    )
                )
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getStudentsByStatus(
        institutionId: String,
        status: String,
        onResult: (List<AppUser>) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        val targetStatus = normalizeStatus(status)

        firestore.collection("users")
            .whereEqualTo("institutionId", institutionId)
            .whereEqualTo("role", "student")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(AppUser::class.java)?.copy(
                        uid = document.id,
                        verificationStatus = normalizeStatus(
                            document.getString("verificationStatus") ?: ""
                        ).ifBlank { "pending" }
                    )
                }.filter { student ->
                    normalizeStatus(student.verificationStatus) == targetStatus
                }

                onResult(list.sortedBy { it.name.lowercase() })
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getPendingStudents(
        institutionId: String,
        onResult: (List<AppUser>) -> Unit
    ) {
        getStudentsByStatus(
            institutionId = institutionId,
            status = "pending",
            onResult = onResult
        )
    }

    fun getVerifiedStudents(
        institutionId: String,
        onResult: (List<AppUser>) -> Unit
    ) {
        getStudentsByStatus(
            institutionId = institutionId,
            status = "verified",
            onResult = onResult
        )
    }

    fun updateStudentVerificationStatus(
        studentUid: String,
        status: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (studentUid.isBlank()) {
            onResult(false, "Student not found")
            return
        }

        val normalizedStatus = normalizeStatus(status)

        if (!isValidStudentStatus(normalizedStatus)) {
            onResult(false, "Invalid student status")
            return
        }

        firestore.collection("users")
            .document(studentUid)
            .update("verificationStatus", normalizedStatus)
            .addOnSuccessListener {
                onResult(true, statusMessage(normalizedStatus))
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update student status")
            }
    }

    fun approveStudent(
        studentUid: String,
        onResult: (Boolean, String) -> Unit
    ) {
        updateStudentVerificationStatus(
            studentUid = studentUid,
            status = "verified",
            onResult = onResult
        )
    }

    fun rejectStudent(
        studentUid: String,
        onResult: (Boolean, String) -> Unit
    ) {
        updateStudentVerificationStatus(
            studentUid = studentUid,
            status = "rejected",
            onResult = onResult
        )
    }

    fun suspendStudent(
        studentUid: String,
        onResult: (Boolean, String) -> Unit
    ) {
        updateStudentVerificationStatus(
            studentUid = studentUid,
            status = "suspended",
            onResult = onResult
        )
    }

    fun setStudentPending(
        studentUid: String,
        onResult: (Boolean, String) -> Unit
    ) {
        updateStudentVerificationStatus(
            studentUid = studentUid,
            status = "pending",
            onResult = onResult
        )
    }
}