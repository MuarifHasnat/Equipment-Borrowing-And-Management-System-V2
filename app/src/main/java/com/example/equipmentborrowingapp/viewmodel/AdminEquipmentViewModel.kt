package com.example.equipmentborrowingapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.equipmentborrowingapp.data.model.Equipment
import com.example.equipmentborrowingapp.data.repository.EquipmentRepository
import com.example.equipmentborrowingapp.utils.UiState

class AdminEquipmentViewModel(
    private val equipmentRepository: EquipmentRepository = EquipmentRepository()
) {
    var equipmentList by mutableStateOf<List<Equipment>>(emptyList())
        private set

    var equipmentUiState by mutableStateOf<UiState<List<Equipment>>>(UiState.Idle)
        private set

    fun loadEquipment(onLoaded: (() -> Unit)? = null) {
        equipmentUiState = UiState.Loading
        equipmentRepository.getEquipmentList { list ->
            equipmentList = list
            equipmentUiState = UiState.Success(list)
            onLoaded?.invoke()
        }
    }

    fun clearEquipment() {
        equipmentList = emptyList()
        equipmentUiState = UiState.Idle
    }
}