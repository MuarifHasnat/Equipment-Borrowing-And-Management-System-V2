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

    fun loadPendingRequests(
        institutionId: String,
        onLoaded: (() -> Unit)? = null
    ) {
        pendingUiState = UiState.Loading

        requestRepository.getPendingRequests(
            institutionId = institutionId
        ) { list ->
            updatePendingRequestState(list)
            onLoaded?.invoke()
        }
    }

    fun loadApprovedRequests(
        institutionId: String,
        onLoaded: (() -> Unit)? = null
    ) {
        approvedUiState = UiState.Loading

        requestRepository.getApprovedRequests(
            institutionId = institutionId
        ) { list ->
            updateApprovedRequestState(list)
            onLoaded?.invoke()
        }
    }
    private fun updatePendingRequestState(
        list: List<BorrowRequest>
    ) {
        pendingRequests = list
        pendingUiState = UiState.Success(list)
    }

    private fun updateApprovedRequestState(
        list: List<BorrowRequest>
    ) {
        approvedRequests = list
        approvedUiState = UiState.Success(list)
    }
    fun clearAdminRequests() {
        pendingRequests = emptyList()
        approvedRequests = emptyList()
        pendingUiState = UiState.Idle
        approvedUiState = UiState.Idle
    }
}