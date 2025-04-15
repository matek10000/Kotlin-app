package pl.wsei.pam.lab06.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    fun toggleIsDone(task: TodoTask) {
        val updatedTask = task.copy(isDone = !task.isDone)
        viewModelScope.launch {
            repository.updateItem(updatedTask)
        }
    }

    fun toggleDone(task: TodoTask) {
        viewModelScope.launch {
            repository.updateItem(task)
        }
    }

    suspend fun updateItem(task: TodoTask) {
        repository.updateItem(task)
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ListUiState(val items: List<TodoTask> = listOf())
