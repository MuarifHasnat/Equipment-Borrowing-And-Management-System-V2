package com.example.equipmentborrowingapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.data.repository.RequestRepository
import com.example.equipmentborrowingapp.utils.UiState

class RequestViewModel(
    private val requestRepository: RequestRepository = RequestRepository()
) {
    var myRequests by mutableStateOf<List<BorrowRequest>>(emptyList())
        private set

    var myRequestsUiState by mutableStateOf<UiState<List<BorrowRequest>>>(UiState.Idle)
        private set

    fun loadUserRequests(userId: String, onLoaded: (() -> Unit)? = null) {
        myRequestsUiState = UiState.Loading

        requestRepository.getUserRequests(userId) { list ->
            myRequests = list
            myRequestsUiState = UiState.Success(list)
            onLoaded?.invoke()
        }
    }

    fun clearMyRequests() {
        myRequests = emptyList()
        myRequestsUiState = UiState.Idle
    }
}