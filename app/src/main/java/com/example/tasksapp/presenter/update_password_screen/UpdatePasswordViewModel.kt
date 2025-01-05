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
import kotlinx.coroutines.flow.first
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
            confirmPasswordError = if (currentState.confirmPassword == "") {
                "Complete el campo por favor"
            } else if (currentState.confirmPassword != currentState.password) {
                "No coincide con las contraseña"
            } else {
                ""
            }

        )

        _uiState.update {
            it.copy(
                passwordError = errors.passwordError,
                confirmPasswordError = errors.confirmPasswordError
            )
        }

        if (errors.hasErrors()) return

        updatePasswordClick()
    }

    private fun updatePasswordClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                apiService.updatePassword(
                    "Bearer " + dataStore.getJwt().first().toString(),
                    UserDto(
                        password = _uiState.value.password,
                    )
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notification = true,
                    message = "Se cambio la contraseña de manera exitosa"
                )
            } catch (e: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notification = true,
                    message = "Error al cambiar la contraseña"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notification = true,
                    message = "Error al cambiar la contraseña"
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
