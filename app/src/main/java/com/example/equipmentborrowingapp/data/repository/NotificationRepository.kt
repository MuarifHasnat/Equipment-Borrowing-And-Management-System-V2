package com.example.equipmentborrowingapp.data.repository

import com.example.equipmentborrowingapp.data.model.AppNotification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class NotificationRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun sendNotification(notification: AppNotification) {
        val docRef = firestore.collection("notifications").document()

        val cleanNotification = notification.copy(
            id = docRef.id,
            institutionId = notification.institutionId.trim(),
            userId = notification.userId.trim(),
            role = notification.role.trim().lowercase(),
            title = notification.title.trim(),
            message = notification.message.trim(),
            type = notification.type.trim().ifBlank { "info" },
            read = false,
            timestamp = if (notification.timestamp > 0L) {
                notification.timestamp
            } else {
                System.currentTimeMillis()
            }
        )

        docRef.set(cleanNotification)
    }

    fun sendNotificationToStudent(
        institutionId: String,
        studentUserId: String,
        title: String,
        message: String,
        type: String = "info"
    ) {
        if (studentUserId.isBlank()) return

        sendNotification(
            AppNotification(
                institutionId = institutionId.trim(),
                userId = studentUserId.trim(),
                role = "student",
                title = title.trim(),
                message = message.trim(),
                type = type.trim().ifBlank { "info" },
                read = false,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun sendNotificationToInstitutionAdmins(
        institutionId: String,
        title: String,
        message: String,
        type: String = "info"
    ) {
        val cleanInstitutionId = institutionId.trim()

        if (cleanInstitutionId.isBlank()) return

        sendNotification(
            AppNotification(
                institutionId = cleanInstitutionId,
                userId = "",
                role = "admin",
                title = title.trim(),
                message = message.trim(),
                type = type.trim().ifBlank { "info" },
                read = false,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun sendNotificationToSuperAdmin(
        title: String,
        message: String,
        type: String = "info"
    ) {
        sendNotification(
            AppNotification(
                institutionId = "",
                userId = "",
                role = "super_admin",
                title = title.trim(),
                message = message.trim(),
                type = type.trim().ifBlank { "info" },
                read = false,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun listenToNotifications(
        institutionId: String,
        userId: String,
        role: String,
        onChange: (List<AppNotification>) -> Unit
    ): ListenerRegistration {
        val cleanInstitutionId = institutionId.trim()
        val cleanUserId = userId.trim()
        val cleanRole = role.trim().lowercase()

        return firestore.collection("notifications")
            .whereIn("role", listOf(cleanRole, "all"))
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    onChange(emptyList())
                    return@addSnapshotListener
                }

                val list = snapshot.documents.mapNotNull { document ->
                    document.toObject(AppNotification::class.java)?.copy(
                        id = document.id
                    )
                }.filter { notification ->
                    shouldShowNotification(
                        notification = notification,
                        currentInstitutionId = cleanInstitutionId,
                        currentUserId = cleanUserId,
                        currentRole = cleanRole
                    )
                }.sortedByDescending { notification ->
                    notification.timestamp
                }

                onChange(list)
            }
    }

    private fun shouldShowNotification(
        notification: AppNotification,
        currentInstitutionId: String,
        currentUserId: String,
        currentRole: String
    ): Boolean {
        val notificationRole = notification.role.trim().lowercase()
        val notificationUserId = notification.userId.trim()
        val notificationInstitutionId = notification.institutionId.trim()

        if (notificationRole == "all") {
            return notificationUserId.isBlank() || notificationUserId == currentUserId
        }

        if (notificationRole != currentRole) {
            return false
        }

        return when (currentRole) {
            "student" -> {
                notificationUserId == currentUserId
            }

            "admin" -> {
                notificationUserId.isBlank() &&
                        notificationInstitutionId.isNotBlank() &&
                        notificationInstitutionId == currentInstitutionId
            }

            "super_admin" -> {
                notificationUserId.isBlank()
            }

            else -> false
        }
    }

    fun markAsRead(notificationId: String) {
        if (notificationId.isBlank()) return

        firestore.collection("notifications")
            .document(notificationId)
            .update("read", true)
    }

    fun markAllAsRead(
        notificationList: List<AppNotification>
    ) {
        notificationList
            .filter { !it.read && it.id.isNotBlank() }
            .forEach { notification ->
                markAsRead(notification.id)
            }
    }

    fun deleteNotification(notificationId: String) {
        if (notificationId.isBlank()) return

        firestore.collection("notifications")
            .document(notificationId)
            .delete()
    }

    fun deleteAllNotifications(
        notificationList: List<AppNotification>
    ) {
        notificationList
            .filter { it.id.isNotBlank() }
            .forEach { notification ->
                deleteNotification(notification.id)
            }
    }

    fun createTestNotification(
        institutionId: String,
        userId: String,
        role: String
    ) {
        val cleanRole = role.trim().lowercase()

        when (cleanRole) {
            "student" -> {
                sendNotificationToStudent(
                    institutionId = institutionId,
                    studentUserId = userId,
                    title = "Test Notification",
                    message = "Your student notification system is working successfully.",
                    type = "info"
                )
            }

            "admin" -> {
                sendNotificationToInstitutionAdmins(
                    institutionId = institutionId,
                    title = "Test Notification",
                    message = "Your admin notification system is working successfully.",
                    type = "info"
                )
            }

            "super_admin" -> {
                sendNotificationToSuperAdmin(
                    title = "Test Notification",
                    message = "Your super admin notification system is working successfully.",
                    type = "info"
                )
            }
        }
    }
}