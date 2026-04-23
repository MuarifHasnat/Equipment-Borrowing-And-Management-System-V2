package com.example.equipmentborrowingapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.equipmentborrowingapp.data.model.BorrowRequest
import com.example.equipmentborrowingapp.data.repository.RequestRepository
import com.example.equipmentborrowingapp.utils.UiState

class AdminRequestViewModel(
    private val requestRepository: RequestRepository = RequestRepository()
) {
    var pendingRequests by mutableStateOf<List<BorrowRequest>>(emptyList())
        private set

    var approvedRequests by mutableStateOf<List<BorrowRequest>>(emptyList())
        private set

    var pendingUiState by mutableStateOf<UiState<List<BorrowRequest>>>(UiState.Idle)
        private set

    var approvedUiState by mutableStateOf<UiState<List<BorrowRequest>>>(UiState.Idle)
        private set

    fun loadPendingRequests(onLoaded: (() -> Unit)? = null) {
        pendingUiState = UiState.Loading
        requestRepository.getPendingRequests { list ->
            pendingRequests = list
            pendingUiState = UiState.Success(list)
            onLoaded?.invoke()
        }
    }

    fun loadApprovedRequests(onLoaded: (() -> Unit)? = null) {
        approvedUiState = UiState.Loading
        requestRepository.getApprovedRequests { list ->
            approvedRequests = list
            approvedUiState = UiState.Success(list)
            onLoaded?.invoke()
        }
    }

    fun clearAdminRequests() {
        pendingRequests = emptyList()
        approvedRequests = emptyList()
        pendingUiState = UiState.Idle
        approvedUiState = UiState.Idle
    }
}