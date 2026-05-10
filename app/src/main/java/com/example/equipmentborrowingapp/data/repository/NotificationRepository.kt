package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.AppNotification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class NotificationRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun sendNotification(notification: AppNotification) {
        val docRef = firestore.collection("notifications").document()

        val newNotification = notification.copy(
            id = docRef.id
        )

        docRef.set(newNotification)
    }

    fun listenToNotifications(
        userId: String,
        role: String,
        onChange: (List<AppNotification>) -> Unit
    ): ListenerRegistration {
        return firestore.collection("notifications")
            .whereIn("role", listOf(role, "all"))
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { document ->
                        document.toObject(AppNotification::class.java)?.copy(
                            id = document.id
                        )
                    }
                        .filter { notification ->
                            notification.userId == userId ||
                                    notification.role == role ||
                                    notification.role == "all"
                        }
                        .sortedByDescending { notification ->
                            notification.timestamp
                        }

                    onChange(list)
                }
            }
    }

    fun markAsRead(notificationId: String) {
        if (notificationId.isBlank()) return

        firestore.collection("notifications")
            .document(notificationId)
            .update("read", true)
    }

    fun deleteNotification(notificationId: String) {
        if (notificationId.isBlank()) return

        firestore.collection("notifications")
            .document(notificationId)
            .delete()
    }

    fun createTestNotification(
        userId: String,
        role: String
    ) {
        val docRef = firestore.collection("notifications").document()

        val notification = AppNotification(
            id = docRef.id,
            userId = userId,
            role = role,
            title = "Test Notification",
            message = "Your notification system is working successfully.",
            type = "info",
            read = false,
            timestamp = System.currentTimeMillis()
        )

        docRef.set(notification)
    }
}