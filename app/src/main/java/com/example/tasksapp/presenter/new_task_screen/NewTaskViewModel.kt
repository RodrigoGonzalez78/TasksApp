package com.example.tasksapp.presenter.new_task_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.remote.ApiService
import com.example.tasksapp.data.remote.dto.TaskDto
import com.example.tasksapp.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    fun loadTask(id: String) {
        viewModelScope.launch {
            try {



                val task = apiService.getTask(
                    "Bearer " + dataStore.getJwt().first(),
                    id.toInt()
                )
                _uiState.update {
                    it.copy(
                        id = id,
                        title = task.title?:"",
                        description = task.description?:""
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        notification = true,
                        message = "Error al cargar la tarea"
                    )
                }
            }
        }
    }

    fun validateFields() {
        val currentState = _uiState.value
        val errors = NewTaskUiState(
            titleError = if (currentState.title.isBlank()) "Complete el campo por favor" else "",
            descriptionError = if (currentState.description.isBlank()) "Complete el campo por favor" else ""
        )
        _uiState.update {
            it.copy(
                titleError = errors.titleError,
                descriptionError = errors.descriptionError
            )
        }

        if (errors.hasErrors()) return
        saveTask()
    }

    private fun saveTask() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val jwt = "Bearer " + dataStore.getJwt().first()
                if (_uiState.value.id == null) {

                    apiService.createTask(
                        jwt,
                        TaskDto(
                            title = _uiState.value.title,
                            description = _uiState.value.description
                        )
                    )
                    _uiState.update { it.copy(message = "Tarea creada") }
                } else {
                    apiService.updateTask(
                        jwt,
                        _uiState.value.id!!.toInt(),
                        TaskDto(
                            title = _uiState.value.title,
                            description = _uiState.value.description
                        )
                    )
                    _uiState.update { it.copy(message = "Tarea actualizada") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(message = "Error al guardar la tarea") }
            } finally {
                _uiState.update { it.copy(isLoading = false, notification = true) }
            }
        }
    }
}

data class NewTaskUiState(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val titleError: String = "",
    val descriptionError: String = "",
    val notification: Boolean = false,
    val isLoading: Boolean = false,
    val message: String = ""
) {
    fun hasErrors() = titleError.isNotEmpty() || descriptionError.isNotEmpty()
}