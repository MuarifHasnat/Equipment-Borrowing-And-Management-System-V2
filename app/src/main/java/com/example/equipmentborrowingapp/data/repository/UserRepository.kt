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
            "blocked",
            "suspended"
        )
    }
    private fun statusMessage(status: String): String {
        return when (normalizeStatus(status)) {
            "pending" -> "Student status changed to pending"
            "verified" -> "Student verified successfully"
            "rejected" -> "Student rejected successfully"
            "blocked" -> "Student blocked successfully"
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
    fun getUserById(
        userId: String,
        onResult: (AppUser?) -> Unit
    ) {
        if (userId.isBlank()) {
            onResult(null)
            return
        }

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(AppUser::class.java)?.copy(
                    uid = document.id,
                    role = document.getString("role")?.trim()?.lowercase().orEmpty(),
                    institutionId = document.getString("institutionId")?.trim().orEmpty(),
                    verificationStatus = document.getString("verificationStatus")
                        ?.trim()
                        ?.lowercase()
                        .orEmpty()
                        .ifBlank { "pending" },
                    studentId = document.getString("studentId")?.trim().orEmpty(),
                    department = document.getString("department")?.trim().orEmpty(),
                    semester = document.getString("semester")?.trim().orEmpty(),
                    phone = document.getString("phone")?.trim().orEmpty(),
                    profileImageUrl = document.getString("profileImageUrl")?.trim().orEmpty()
                )

                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun updateStudentProfile(
        userId: String,
        phone: String,
        profileImageUrl: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (userId.isBlank()) {
            onResult(false, "User not found")
            return
        }

        firestore.collection("users")
            .document(userId)
            .update(
                mapOf(
                    "phone" to phone.trim(),
                    "profileImageUrl" to profileImageUrl.trim()
                )
            )
            .addOnSuccessListener {
                onResult(true, "Profile updated successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update profile")
            }
    }

    fun updateAdminProfile(
        userId: String,
        phone: String,
        profileImageUrl: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (userId.isBlank()) {
            onResult(false, "User not found")
            return
        }

        firestore.collection("users")
            .document(userId)
            .update(
                mapOf(
                    "phone" to phone.trim(),
                    "profileImageUrl" to profileImageUrl.trim()
                )
            )
            .addOnSuccessListener {
                onResult(true, "Profile updated successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update profile")
            }
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