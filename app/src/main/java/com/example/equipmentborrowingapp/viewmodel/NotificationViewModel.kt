package com.example.equipmentborrowingapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.equipmentborrowingapp.data.model.AppNotification
import com.example.equipmentborrowingapp.data.repository.NotificationRepository
import com.google.firebase.firestore.ListenerRegistration

class NotificationViewModel(
    private val notificationRepository: NotificationRepository = NotificationRepository()
) {
    var notificationList by mutableStateOf<List<AppNotification>>(emptyList())
        private set

    var unreadCount by mutableStateOf(0)
        private set

    private var listenerRegistration: ListenerRegistration? = null

    fun startListening(userId: String, role: String) {
        stopListening()

        listenerRegistration = notificationRepository.listenToNotifications(
            userId = userId,
            role = role
        ) { list ->
            notificationList = list
            unreadCount = list.count { !it.isRead }
        }
    }

    fun markAsRead(notificationId: String) {
        notificationRepository.markAsRead(notificationId)
    }

    fun deleteNotification(notificationId: String) {
        notificationRepository.deleteNotification(notificationId)
    }

    fun stopListening() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    fun clearNotifications() {
        stopListening()
        notificationList = emptyList()
        unreadCount = 0
    }
}