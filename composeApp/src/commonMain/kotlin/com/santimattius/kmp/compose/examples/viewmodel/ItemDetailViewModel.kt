package com.santimattius.kmp.compose.examples.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class DetailUiState(
    val itemId: String = "",
    val viewCount: Int = 0,
    val viewModelId: String = "",
)

/**
 * Example 5: ViewModel scoped to the back stack entry lifecycle.
 *
 * Created with viewModel { } + rememberViewModelStoreNavEntryDecorator() in NavDisplay.
 *
 * Lifecycle:
 * - CREATED: when navigating to the entry that references it.
 * - ALIVE: while the entry remains in the back stack (survives recompositions).
 * - DESTROYED: when the entry leaves the back stack (pop or cleanup).
 *
 * The unique viewModelId allows visual verification that the ViewModel
 * is not recreated on every recomposition but persists.
 */
@OptIn(ExperimentalUuidApi::class)
class ItemDetailViewModel(itemId: String) : ViewModel() {

    private val _state = MutableStateFlow(
        DetailUiState(
            itemId = itemId,
            viewCount = 0,
            viewModelId = Uuid.random().toString().take(8),
        )
    )
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    fun incrementViewCount() {
        viewModelScope.launch {
            _state.update { it.copy(viewCount = it.viewCount + 1) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // In production: cancel jobs, release resources
        // Called when the entry leaves the back stack
    }
}
