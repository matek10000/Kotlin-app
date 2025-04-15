package pl.wsei.pam.lab06.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import pl.wsei.pam.lab06.data.model.TodoTask
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository

class ListViewModel(private val repository: TodoTaskRepository) : ViewModel() {

    val listUiState: StateFlow<ListUiState> = repository.getAllAsStream()
        .map { ListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ListUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ListUiState(val items: List<TodoTask> = listOf())
