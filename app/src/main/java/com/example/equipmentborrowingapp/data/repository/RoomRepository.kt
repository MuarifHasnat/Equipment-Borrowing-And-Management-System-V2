package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.Room
import com.google.firebase.firestore.FirebaseFirestore

class RoomRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun addRoom(
        institutionId: String,
        name: String,
        building: String,
        floor: String,
        roomType: String,
        department: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(false, "Institution not found")
            return
        }

        if (name.isBlank()) {
            onResult(false, "Room/Lab name is required")
            return
        }

        val docRef = firestore.collection("Room").document()

        val room = Room(
            id = docRef.id,
            institutionId = institutionId,
            name = name.trim(),
            building = building.trim(),
            floor = floor.trim(),
            roomType = roomType.trim(),
            department = department.trim(),
            active = true,
            createdAt = System.currentTimeMillis()
        )

        docRef.set(room)
            .addOnSuccessListener {
                onResult(true, "Room/Lab added successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to add room/lab")
            }
    }

    fun getRooms(
        institutionId: String,
        onResult: (List<Room>) -> Unit
    ) {
        if (institutionId.isBlank()) {
            onResult(emptyList())
            return
        }

        firestore.collection("Room")
            .whereEqualTo("institutionId", institutionId)
            .whereEqualTo("active", true)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { document ->
                    document.toObject(Room::class.java)?.copy(
                        id = document.id
                    )
                }

                onResult(list.sortedBy { it.name })
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun updateRoom(
        room: Room,
        onResult: (Boolean, String) -> Unit
    ) {
        if (room.id.isBlank()) {
            onResult(false, "Room not found")
            return
        }

        firestore.collection("Room")
            .document(room.id)
            .set(room)
            .addOnSuccessListener {
                onResult(true, "Room updated successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to update room")
            }
    }

    fun deleteRoom(
        roomId: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (roomId.isBlank()) {
            onResult(false, "Room not found")
            return
        }

        firestore.collection("Room")
            .document(roomId)
            .update("active", false)
            .addOnSuccessListener {
                onResult(true, "Room deleted successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to delete room")
            }
    }
}