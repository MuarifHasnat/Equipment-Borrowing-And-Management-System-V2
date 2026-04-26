package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.AppNotification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class NotificationRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // 🔔 Send notification
    fun sendNotification(notification: AppNotification) {
        val id = firestore.collection("notifications").document().id

        val newNotification = notification.copy(id = id)

        firestore.collection("notifications")
            .document(id)
            .set(newNotification)
    }

    // 🔴 Listen to user notifications (REAL-TIME)
    fun listenToNotifications(
        userId: String,
        role: String,
        onChange: (List<AppNotification>) -> Unit
    ): ListenerRegistration {

        return firestore.collection("notifications")
            .whereIn("role", listOf(role, "all"))
            .addSnapshotListener { snapshot, _ ->

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull {
                        it.toObject(AppNotification::class.java)
                    }
                        .filter {
                            it.userId == userId || it.role == role || it.role == "all"
                        }
                        .sortedByDescending { it.timestamp }

                    onChange(list)
                }
            }
    }

    // ✅ Mark notification as read
    fun markAsRead(notificationId: String) {
        firestore.collection("notifications")
            .document(notificationId)
            .update("isRead", true)
    }

    // 🧹 Delete notification
    fun deleteNotification(notificationId: String) {
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
            isRead = false,
            timestamp = System.currentTimeMillis()
        )

        docRef.set(notification)
    }
}