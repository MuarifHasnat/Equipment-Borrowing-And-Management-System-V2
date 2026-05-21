package com.example.equipmentborrowingapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.equipmentborrowingapp.data.model.AppNotification
import com.example.equipmentborrowingapp.data.repository.NotificationRepository
import com.google.firebase.firestore.ListenerRegistration

class NotificationViewModel : ViewModel() {

    private val repository = NotificationRepository()

    var notificationList by mutableStateOf<List<AppNotification>>(emptyList())
        private set

    private var listenerRegistration: ListenerRegistration? = null

    fun startListening(
        institutionId: String,
        userId: String,
        role: String
    ) {
        stopListening()

        listenerRegistration = repository.listenToNotifications(
            institutionId = institutionId,
            userId = userId,
            role = role
        ) { list ->
            notificationList = list
        }
    }

    fun markAsRead(notificationId: String) {
        repository.markAsRead(notificationId)
    }

    fun markAllAsRead() {
        repository.markAllAsRead(notificationList)
    }

    fun deleteNotification(notificationId: String) {
        repository.deleteNotification(notificationId)
    }

    fun deleteAllNotifications() {
        repository.deleteAllNotifications(notificationList)
    }

    fun clearNotifications() {
        notificationList = emptyList()
        stopListening()
    }

    private fun stopListening() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }
}