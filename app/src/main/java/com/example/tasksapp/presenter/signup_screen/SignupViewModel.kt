package com.example.tasksapp.presenter.signup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.remote.ApiService
import com.example.tasksapp.data.remote.dto.UserDto
import com.example.tasksapp.presenter.login_screen.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()


    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onLastNameChange(newLastName: String) {
        _uiState.update { it.copy(lastName = newLastName) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = newConfirmPassword) }
    }

    fun validateAndRegister() {
        val currentState = _uiState.value
        val errors = SignupUiState(
            nameError = if (currentState.name.isBlank()) "El nombre no puede estar vacío" else "",
            lastNameError = if (currentState.lastName.isBlank()) "El apellido no puede estar vacío" else "",
            emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email)
                    .matches()
            ) "Formato de email inválido" else "",
            passwordError = if (currentState.password.length < 8) "La contraseña debe tener al menos 8 caracteres" else "",
            confirmPasswordError = if (currentState.password != currentState.confirmPassword) "Las contraseñas no coinciden" else ""
        )
        _uiState.update {
            it.copy(
                nameError = errors.nameError,
                lastNameError = errors.lastNameError,
                emailError = errors.lastNameError,
                passwordError = errors.passwordError,
                confirmPasswordError = errors.confirmPasswordError
            )
        }

        if (errors.hasErrors()) return

        register()
    }

    fun register() {
        viewModelScope.launch {
            val currentState = _uiState.value

            if (currentState.password != currentState.confirmPassword) {
                _uiState.update { it.copy(message = "Las contraseñas no coinciden") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, message = "") }

            try {
                val user = UserDto(
                    firstName = _uiState.value.name,
                    lastName = _uiState.value.lastName,
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
                apiService.signUp(user)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        message = "Registro exitoso"
                    )
                }
            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = "Error en el registro: ${e.message}"
                    )
                }
            }
        }
    }
}

data class SignupUiState(
    val email: String = "",
    val name: String = "",
    val lastName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val message: String = "",
    val nameError: String = "",
    val lastNameError: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = ""
) {
    fun hasErrors() =
        nameError.isNotEmpty() || lastNameError.isNotEmpty() || emailError.isNotEmpty() || passwordError.isNotEmpty() || confirmPasswordError.isNotEmpty()
}