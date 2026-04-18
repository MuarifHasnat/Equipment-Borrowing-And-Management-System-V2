package com.example.equipmentborrowingapp.ui.auth

import androidx.lifecycle.ViewModel
import com.example.equipmentborrowingapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Represents the different states our Auth screens can be in
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val role: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password are required")
            return
        }

        _authState.value = AuthState.Loading

        authRepository.loginUser(email, password) { success, message ->
            if (success) {
                val uid = authRepository.getCurrentUserUid()
                if (uid != null) {
                    authRepository.getUserRole(uid) { role ->
                        if (role != null) {
                            _authState.value = AuthState.Success(role)
                        } else {
                            _authState.value = AuthState.Error("Unknown user role")
                        }
                    }
                } else {
                    _authState.value = AuthState.Error("User ID not found")
                }
            } else {
                _authState.value = AuthState.Error(message)
            }
        }
    }

    fun register(name: String, email: String, password: String, role: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || role.isBlank()) {
            _authState.value = AuthState.Error("Please fill in all required fields")
            return
        }

        _authState.value = AuthState.Loading

        authRepository.registerUser(name, email, password, role.lowercase()) { success, message ->
            if (success) {
                _authState.value = AuthState.Success("registered")
            } else {
                _authState.value = AuthState.Error(message)
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}