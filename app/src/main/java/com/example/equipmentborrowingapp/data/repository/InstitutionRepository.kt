package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.Institution
import com.google.firebase.firestore.FirebaseFirestore

class InstitutionRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getApprovedInstitutions(
        onResult: (List<Institution>) -> Unit
    ) {
        firestore.collection("institutions")
            .whereEqualTo("status", "Approved")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(Institution::class.java)?.copy(
                        id = document.id
                    )
                }

                onResult(list.sortedBy { it.name })
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getAllInstitutions(
        onResult: (List<Institution>) -> Unit
    ) {
        firestore.collection("institutions")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(Institution::class.java)?.copy(
                        id = document.id
                    )
                }

                onResult(list.sortedBy { it.name })
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun updateInstitutionStatus(
        institutionId: String,
        status: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(false, "Institution not found")
            return
        }

        firestore.collection("institutions")
            .document(institutionId)
            .update("status", status)
            .addOnSuccessListener {
                onResult(true, "Institution status updated")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update institution")
            }
    }
}