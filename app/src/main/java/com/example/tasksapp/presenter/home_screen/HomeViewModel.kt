package com.example.tasksapp.presenter.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasksapp.data.remote.ApiService
import com.example.tasksapp.data.remote.dto.TaskDto
import com.example.tasksapp.data.remote.dto.toDomainModel
import com.example.tasksapp.data.repository.DataStoreRepository
import com.example.tasksapp.presenter.login_screen.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskDto>>(emptyList())
    val tasks: StateFlow<List<TaskDto>> = _tasks.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun closeSession() {
        viewModelScope.launch {
            dataStore.deleteJwt()
        }
    }

    fun getAllTasks() {
        viewModelScope.launch {
            try {
                val taskDtos = apiService.getTasks("Bearer "+ dataStore.getJwt().first().toString())
                // val taskModels = taskDtos.map { it.toDomainModel() } // Convertir DTOs a modelos
                _tasks.value = taskDtos
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteTask(id:Int){
        viewModelScope.launch {
            try {
                apiService.deleteTask("Bearer "+ dataStore.getJwt().first().toString(),id)
                getAllTasks()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}