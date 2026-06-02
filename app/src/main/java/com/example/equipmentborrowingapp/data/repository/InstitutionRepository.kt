package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.Institution
import com.google.firebase.firestore.FirebaseFirestore
import com.example.equipmentborrowingapp.data.model.InstitutionAdminRequest
private const val INSTITUTIONS_COLLECTION = "institutions"
private const val INSTITUTION_ADMIN_REQUESTS_COLLECTION = "institution_admin_requests"

class InstitutionRepository {

    private val firestore = FirebaseFirestore.getInstance()
    fun createInstitutionAdminRequest(
        institutionId: String,
        institutionName: String,
        adminName: String,
        adminEmail: String,
        createdBy: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val normalizedInstitutionId = institutionId.trim()
        val normalizedInstitutionName = institutionName.trim()
        val normalizedAdminName = adminName.trim()
        val normalizedAdminEmail = adminEmail.trim().lowercase()

        if (
            normalizedInstitutionId.isBlank() ||
            normalizedInstitutionName.isBlank() ||
            normalizedAdminName.isBlank() ||
            normalizedAdminEmail.isBlank() ||
            createdBy.isBlank()
        ) {
            onResult(false, "Required fields are missing")
            return
        }

        val docRef = firestore.collection(INSTITUTION_ADMIN_REQUESTS_COLLECTION).document()

        val request = mapOf(
            "id" to docRef.id,
            "institutionId" to normalizedInstitutionId,
            "institutionName" to normalizedInstitutionName,
            "adminName" to normalizedAdminName,
            "adminEmail" to normalizedAdminEmail,
            "status" to "Pending",
            "createdBy" to createdBy,
            "createdAt" to System.currentTimeMillis()
        )

        docRef.set(request)
            .addOnSuccessListener {
                onResult(true, "Institution admin request saved successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to save admin request")
            }
    }
    fun createInstitution(
        institutionId: String,
        name: String,
        shortName: String,
        emailDomain: String,
        type: String,
        status: String,
        createdBy: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val normalizedInstitutionId = institutionId.trim()
        val normalizedName = name.trim()
        val normalizedShortName = shortName.trim()
        val normalizedEmailDomain = emailDomain.trim().lowercase()
        val normalizedType = type.trim().lowercase()
        val normalizedStatus = status.trim()

        if (
            normalizedInstitutionId.isBlank() ||
            normalizedName.isBlank() ||
            normalizedShortName.isBlank() ||
            normalizedEmailDomain.isBlank() ||
            normalizedType.isBlank() ||
            normalizedStatus.isBlank()
        ) {
            onResult(false, "Required fields are missing")
            return
        }

        val docRef = firestore.collection(INSTITUTIONS_COLLECTION)
            .document(normalizedInstitutionId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onResult(false, "Institution ID already exists")
                    return@addOnSuccessListener
                }

                val institution = Institution(
                    id = normalizedInstitutionId,
                    name = normalizedName,
                    shortName = normalizedShortName,
                    emailDomain = normalizedEmailDomain,
                    type = normalizedType,
                    status = normalizedStatus,
                    createdBy = createdBy,
                    createdAt = System.currentTimeMillis()
                )

                docRef.set(institution)
                    .addOnSuccessListener {
                        onResult(true, "Institution created successfully")
                    }
                    .addOnFailureListener { e ->
                        onResult(false, e.message ?: "Failed to create institution")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to check institution")
            }
    }
    fun getApprovedInstitutions(
        onResult: (List<Institution>) -> Unit
    ) {
        firestore.collection(INSTITUTIONS_COLLECTION)
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
        firestore.collection(INSTITUTIONS_COLLECTION)
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

        firestore.collection(INSTITUTIONS_COLLECTION)
            .document(institutionId)
            .update("status", status)
            .addOnSuccessListener {
                onResult(true, "Institution status updated")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update institution")
            }
    }
    fun getInstitutionAdminRequests(
        onResult: (List<InstitutionAdminRequest>) -> Unit
    ) {
        firestore.collection(INSTITUTION_ADMIN_REQUESTS_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(InstitutionAdminRequest::class.java)?.copy(
                        id = document.id
                    )
                }

                onResult(list.sortedByDescending { it.createdAt })
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun updateInstitutionAdminRequestStatus(
        requestId: String,
        status: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (requestId.isBlank()) {
            onResult(false, "Admin request not found")
            return
        }

        firestore.collection(INSTITUTION_ADMIN_REQUESTS_COLLECTION)
            .document(requestId)
            .update(
                mapOf(
                    "status" to status,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .addOnSuccessListener {
                onResult(true, "Admin request status updated")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update admin request")
            }
    }
}
