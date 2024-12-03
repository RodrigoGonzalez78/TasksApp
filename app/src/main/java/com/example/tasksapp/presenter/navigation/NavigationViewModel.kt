package com.example.tasksapp.presenter.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.repository.DataStoreRepository
import com.example.tasksapp.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    init {
        viewModelScope.launch {
            dataStoreRepository.getJwt().collect { token ->
                _isAuthenticated.value = Utils.IsJwtValid(token)
            }
        }
    }
}
