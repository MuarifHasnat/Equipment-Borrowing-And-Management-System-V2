package com.example.equipmentborrowingapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.equipmentborrowingapp.data.model.Room
import com.example.equipmentborrowingapp.data.repository.RoomRepository
import com.example.equipmentborrowingapp.utils.UiState

class RoomViewModel {

    private val roomRepository = RoomRepository()

    var roomList by mutableStateOf<List<Room>>(emptyList())
        private set

    var roomUiState by mutableStateOf<UiState<List<Room>>>(UiState.Idle)
        private set

    fun loadRooms(
        institutionId: String,
        onLoaded: (() -> Unit)? = null
    ) {
        roomUiState = UiState.Loading

        roomRepository.getRooms(
            institutionId = institutionId
        ) { list ->
            roomList = list
            roomUiState = UiState.Success(list)
            onLoaded?.invoke()
        }
    }

    fun clearRooms() {
        roomList = emptyList()
        roomUiState = UiState.Idle
    }
}