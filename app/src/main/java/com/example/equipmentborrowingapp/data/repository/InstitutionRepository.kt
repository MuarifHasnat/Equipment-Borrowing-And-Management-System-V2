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
}