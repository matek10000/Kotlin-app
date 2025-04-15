package pl.wsei.pam.lab06.data.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.wsei.pam.lab06.data.*
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository
import pl.wsei.pam.lab06.data.state.TodoTaskForm
import pl.wsei.pam.lab06.data.state.TodoTaskUiState
import pl.wsei.pam.lab06.data.state.toTodoTask

class FormViewModel(private val repository: TodoTaskRepository) : ViewModel() {

    var todoTaskUiState by mutableStateOf(TodoTaskUiState())
        private set

    fun updateUiState(todoTaskForm: TodoTaskForm) {
        todoTaskUiState = TodoTaskUiState(
            todoTask = todoTaskForm,
            isValid = validate(todoTaskForm)
        )
    }

    private fun validate(uiState: TodoTaskForm = todoTaskUiState.todoTask): Boolean {
        return uiState.title.isNotBlank()
    }

    fun save(onSaved: () -> Unit) {
        if (!todoTaskUiState.isValid) return
        viewModelScope.launch {
            repository.insertItem(todoTaskUiState.todoTask.toTodoTask())
            onSaved()
        }
    }
}
