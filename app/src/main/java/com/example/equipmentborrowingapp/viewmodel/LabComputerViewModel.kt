package com.example.equipmentborrowingapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.equipmentborrowingapp.data.model.LabComputer
import com.example.equipmentborrowingapp.data.repository.LabComputerRepository
import com.example.equipmentborrowingapp.utils.UiState

class LabComputerViewModel(
    private val labComputerRepository: LabComputerRepository = LabComputerRepository()
) {
    var studentLabComputerList by mutableStateOf<List<LabComputer>>(emptyList())
        private set

    var studentLabComputerUiState by mutableStateOf<UiState<List<LabComputer>>>(UiState.Idle)
        private set

    fun loadStudentLabComputers(onLoaded: (() -> Unit)? = null) {
        studentLabComputerUiState = UiState.Loading

        labComputerRepository.getLabComputers { list ->
            studentLabComputerList = list
            studentLabComputerUiState = UiState.Success(list)
            onLoaded?.invoke()
        }
    }

    fun clearStudentLabComputers() {
        studentLabComputerList = emptyList()
        studentLabComputerUiState = UiState.Idle
    }
}