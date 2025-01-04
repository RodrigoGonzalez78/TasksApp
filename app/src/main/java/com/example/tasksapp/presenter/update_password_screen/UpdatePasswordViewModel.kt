package com.example.tasksapp.presenter.update_password_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.remote.ApiService
import com.example.tasksapp.data.remote.dto.UserDto
import com.example.tasksapp.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpdatePasswordUiState())
    val uiState: StateFlow<UpdatePasswordUiState> = _uiState.asStateFlow()

    fun onPasswordChange(email: String) {
        _uiState.update { it.copy(password = email) }
    }

    fun onConfirmPasswordChange(password: String) {
        _uiState.update { it.copy(confirmPassword = password) }

    }

    fun changeNotificationState(newState: Boolean) {
        _uiState.update { it.copy(notification = newState) }
    }

    fun validateFields() {
        val currentState = _uiState.value

        val errors = UpdatePasswordUiState(
            passwordError = if (currentState.password == "") "Complete el campo por favor" else "",
            confirmPasswordError = if (currentState.confirmPassword == "") "Complete el campo por favor" else ""
        )

        _uiState.update {
            it.copy(
                passwordError = errors.passwordError,
                confirmPasswordError = errors.confirmPasswordError
            )
        }

        if (errors.hasErrors()) return

        loginClick()
    }

    private fun loginClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response = apiService.login(
                    UserDto(
                        email = _uiState.value.password,
                        password = _uiState.value.confirmPassword,
                    )
                )

                dataStore.saveJwt(response.token)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notification = true,
                    message = "Inicio de sesión exitoso."
                )
            } catch (e: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notification = true,
                    message = "Error de inicio de sesión"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notification = true,
                    message = "Error de inicio de sesión"
                )
            }
        }
    }
}

data class UpdatePasswordUiState(
    val password: String = "",
    val confirmPassword: String = "",
    val confirmPasswordError: String = "",
    val passwordError: String = "",
    val isLoading: Boolean = false,
    val notification: Boolean = false,
    val message: String = ""
) {
    fun hasErrors() = this.confirmPasswordError.isNotEmpty() || this.passwordError.isNotEmpty()
}
