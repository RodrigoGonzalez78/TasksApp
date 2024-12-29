package com.example.tasksapp.presenter.new_task_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.remote.ApiService
import com.example.tasksapp.data.remote.dto.TaskDto
import com.example.tasksapp.data.repository.DataStoreRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
class NewTaskViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewTaskUiState())
    val uiState: StateFlow<NewTaskUiState> = _uiState.asStateFlow()


    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }

    }

    fun changeNotificationState(newState: Boolean) {
        _uiState.update { it.copy(notification = newState) }
    }

    fun validateFields() {
        val currentState = _uiState.value
        val errors = NewTaskUiState(
            titleError = if (currentState.title == "") "Complete el campo por favor" else "",
            descriptionError = if (currentState.description == "") "Complete el campo por favor" else ""
        )
        _uiState.update {
            it.copy(
                titleError = errors.titleError,
                descriptionError = errors.descriptionError
            )
        }

        if (errors.hasErrors()) return
        createTasks()
    }

    private fun createTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {

               apiService.createTask(
                   "Bearer "+ dataStore.getJwt().first().toString(),
                    TaskDto(
                        title = _uiState.value.title,
                        description = _uiState.value.description,
                        id = null,
                        done = null,
                        userId = null,
                        createdAt = null,
                        updatedAt = null,
                        deletedAt = null
                    )
                )


                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notification = true,
                    message = "Exito"
                )
            } catch (e: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notification = true,
                    message = "Error"
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

data class NewTaskUiState(
    val title: String = "",
    val description: String = "",
    val titleError: String = "",
    val descriptionError: String = "",
    val notification: Boolean = false,
    val isLoading: Boolean = false,
    val message: String = ""

) {
    fun hasErrors() = this.titleError.isNotEmpty() || this.descriptionError.isNotEmpty()
}