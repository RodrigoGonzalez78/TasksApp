package com.example.tasksapp.presenter.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.remote.ApiService
import com.example.tasksapp.data.remote.dto.LoginRequest
import com.example.tasksapp.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response =
                    apiService.login(LoginRequest(_uiState.value.email, _uiState.value.password))
                    dataStore.saveJwt(response.token)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    message = "Inicio de sesión exitoso. Token: ${response.token}"
                )
            } catch (e: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    message = "Error de inicio de sesión: ${e.message}"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    message = "Error de inicio de sesión: ${e.message}"
                )
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val message: String = ""
)

