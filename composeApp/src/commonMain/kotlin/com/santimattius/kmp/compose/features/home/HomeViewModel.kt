package com.santimattius.kmp.compose.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santimattius.kmp.compose.core.data.PictureRepository
import com.santimattius.kmp.compose.core.domain.Picture
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val isDarkMode: Boolean = false,
    val data: Picture? = null,
)

class HomeViewModel(
    private val repository: PictureRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    private val _isDarkModeState = MutableStateFlow(false)
    val state = _state.combine(_isDarkModeState) { state, isDarkMode ->
        state.copy(isDarkMode = isDarkMode)
    }.onStart {
        fetchPicture()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _state.update { it.copy(isLoading = false, hasError = true) }
    }
    private var dataJob: Job? = null

    private fun fetchPicture() {
        dataJob?.cancel()
        dataJob = viewModelScope.launch(exceptionHandler) {
            _state.update { it.copy(isLoading = true, hasError = false) }
            val result = repository.random()
            result.onSuccess { picture ->
                _state.update { it.copy(isLoading = false, data = picture) }
            }.onFailure {
                _state.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }


    fun randomImage() = fetchPicture()

    fun darkMode() {
        viewModelScope.launch(exceptionHandler) {
            _isDarkModeState.value =  !state.value.isDarkMode
        }
    }
}