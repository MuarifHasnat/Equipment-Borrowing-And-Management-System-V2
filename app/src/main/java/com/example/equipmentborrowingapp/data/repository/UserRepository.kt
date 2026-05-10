package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.AppUser
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getPendingStudents(
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
            .whereEqualTo("verificationStatus", "pending")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(AppUser::class.java)?.copy(
                        uid = document.id
                    )
                }

                onResult(list.sortedBy { it.name })
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getVerifiedStudents(
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
            .whereEqualTo("verificationStatus", "verified")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(AppUser::class.java)?.copy(
                        uid = document.id
                    )
                }

                onResult(list.sortedBy { it.name })
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun approveStudent(
        studentUid: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (studentUid.isBlank()) {
            onResult(false, "Student not found")
            return
        }

        firestore.collection("users")
            .document(studentUid)
            .update("verificationStatus", "verified")
            .addOnSuccessListener {
                onResult(true, "Student verified successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to verify student")
            }
    }

    fun rejectStudent(
        studentUid: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (studentUid.isBlank()) {
            onResult(false, "Student not found")
            return
        }

        firestore.collection("users")
            .document(studentUid)
            .update("verificationStatus", "rejected")
            .addOnSuccessListener {
                onResult(true, "Student rejected successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to reject student")
            }
    }
}