package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.Equipment
import com.google.firebase.firestore.FirebaseFirestore
private const val EQUIPMENT_COLLECTION = "equipment"
private const val BORROW_REQUESTS_COLLECTION = "borrow_requests"
class EquipmentRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun addEquipment(
        institutionId: String,
        roomId: String,
        name: String,
        description: String,
        condition: String,
        totalQuantity: Int,
        availableQuantity: Int,
        category: String,
        imageName: String,
        imageUrl: String,
        isBorrowable: Boolean,
        onResult: (Boolean, String) -> Unit
    )

    {
        if (institutionId.isBlank()) {
            onResult(false, "Institution not found")
            return
        }
        if (roomId.isBlank()) {
            onResult(false, "Room/Lab not selected")
            return
        }
        val docRef = firestore.collection(EQUIPMENT_COLLECTION).document()

        val equipment = Equipment(
            id = docRef.id,
            institutionId = institutionId,
            roomId = roomId,
            name = name.trim(),
            description = description.trim(),
            condition = condition.trim(),
            totalQuantity = totalQuantity,
            availableQuantity = availableQuantity,
            category = category.trim(),
            imageName = imageName.trim(),
            imageUrl = imageUrl.trim(),
            isBorrowable = isBorrowable,
            borrowType = if (isBorrowable) "OutsideLab" else "LabUseOnly",
            addedBy = "",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        docRef.set(equipment)
            .addOnSuccessListener {
                onResult(true, "Equipment added successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to add equipment")
            }
    }

    fun getEquipmentList(
        institutionId: String,
        roomId: String? = null,
        onResult: (List<Equipment>) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        var query =firestore.collection(EQUIPMENT_COLLECTION)
            .whereEqualTo("institutionId", institutionId)

        if (!roomId.isNullOrBlank()) {
            query = query.whereEqualTo("roomId", roomId)
        }

        query.get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(Equipment::class.java)?.copy(
                        id = document.id
                    )
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
    fun decreaseAvailableQuantity(
        equipmentId: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val docRef = firestore.collection(EQUIPMENT_COLLECTION).document(equipmentId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val currentAvailable = snapshot.getLong("availableQuantity")?.toInt() ?: 0
            val isBorrowable = snapshot.getBoolean("isBorrowable") ?: true

            if (!isBorrowable) {
                throw Exception("This equipment is not borrowable")
            }

            if (currentAvailable > 0) {
                transaction.update(docRef, "availableQuantity", currentAvailable - 1)
            } else {
                throw Exception("No available quantity left")
            }
        }.addOnSuccessListener {
            onResult(true, "Available quantity decreased")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to decrease quantity")
        }
    }

    fun increaseAvailableQuantity(
        equipmentId: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val docRef = firestore.collection(EQUIPMENT_COLLECTION).document(equipmentId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val currentAvailable = snapshot.getLong("availableQuantity")?.toInt() ?: 0
            val totalQuantity = snapshot.getLong("totalQuantity")?.toInt() ?: 0

            if (currentAvailable < totalQuantity) {
                transaction.update(docRef, "availableQuantity", currentAvailable + 1)
            } else {
                throw Exception("Available quantity cannot exceed total quantity")
            }
        }.addOnSuccessListener {
            onResult(true, "Available quantity increased")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "Failed to increase quantity")
        }
    }

    fun updateEquipment(
        equipment: Equipment,
        onResult: (Boolean, String) -> Unit
    ) {
        if (equipment.id.isBlank()) {
            onResult(false, "Invalid equipment id")
            return
        }

        if (equipment.name.isBlank()) {
            onResult(false, "Equipment name is required")
            return
        }

        if (equipment.description.isBlank()) {
            onResult(false, "Description is required")
            return
        }

        if (equipment.condition.isBlank()) {
            onResult(false, "Condition is required")
            return
        }

        if (equipment.category.isBlank()) {
            onResult(false, "Category is required")
            return
        }

        if (equipment.imageName.isBlank() && equipment.imageUrl.isBlank()) {
            onResult(false, "Provide either image name or image URL")
            return
        }

        if (equipment.totalQuantity < 0 || equipment.availableQuantity < 0) {
            onResult(false, "Quantity cannot be negative")
            return
        }

        if (equipment.availableQuantity > equipment.totalQuantity) {
            onResult(false, "Available quantity cannot exceed total quantity")
            return
        }

        val docRef = firestore.collection(EQUIPMENT_COLLECTION).document(equipment.id)

        docRef.set(
            equipment.copy(
                name = equipment.name.trim(),
                description = equipment.description.trim(),
                category = equipment.category.trim(),
                condition = equipment.condition.trim(),
                imageName = equipment.imageName.trim(),
                imageUrl = equipment.imageUrl.trim()
            )
        )
            .addOnSuccessListener {
                onResult(true, "Equipment updated successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update equipment")
            }
    }

    fun deleteEquipment(
        equipmentId: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (equipmentId.isBlank()) {
            onResult(false, "Invalid equipment id")
            return
        }

        firestore.collection(BORROW_REQUESTS_COLLECTION)
            .whereEqualTo("equipmentId", equipmentId)
            .get()
            .addOnSuccessListener { result ->
                val hasActiveRequest = result.documents.any { document ->
                    val status = document.getString("status")?.trim().orEmpty()
                    status.equals("Pending", ignoreCase = true) ||
                            status.equals("Approved", ignoreCase = true) ||
                            status.equals("Overdue", ignoreCase = true)
                }

                if (hasActiveRequest) {
                    onResult(false, "Cannot delete equipment with active requests")
                    return@addOnSuccessListener
                }

                firestore.collection(EQUIPMENT_COLLECTION)
                    .document(equipmentId)
                    .delete()
                    .addOnSuccessListener {
                        onResult(true, "Equipment deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        onResult(false, e.message ?: "Failed to delete equipment")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to check related requests")
            }
    }
}