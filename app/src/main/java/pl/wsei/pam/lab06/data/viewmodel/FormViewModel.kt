package pl.wsei.pam.lab06.data.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.wsei.pam.lab06.TodoApplication
import pl.wsei.pam.lab06.data.provider.CurrentDateProvider
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository
import pl.wsei.pam.lab06.data.state.TodoTaskForm
import pl.wsei.pam.lab06.data.state.TodoTaskUiState
import pl.wsei.pam.lab06.data.state.toTodoTask
import pl.wsei.pam.lab06.notifications.updateAlarmForNearestTask

class FormViewModel(
    private val repository: TodoTaskRepository,
    private val dateProvider: CurrentDateProvider
) : ViewModel() {

    var todoTaskUiState by mutableStateOf(TodoTaskUiState())
        private set

    fun updateUiState(todoTaskForm: TodoTaskForm) {
        todoTaskUiState = TodoTaskUiState(
            todoTask = todoTaskForm,
            isValid = validate(todoTaskForm)
        )
    }

    private fun validate(uiState: TodoTaskForm = todoTaskUiState.todoTask): Boolean {
        val deadlineDate = java.time.Instant.ofEpochMilli(uiState.deadline)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
        return uiState.title.isNotBlank() && deadlineDate.isAfter(dateProvider.currentDate)
    }

    fun save(context: Context, onSaved: () -> Unit) {
        if (!todoTaskUiState.isValid) return
        viewModelScope.launch {
            repository.insertItem(todoTaskUiState.todoTask.toTodoTask())

            val tasks = repository.getAllNow()
            updateAlarmForNearestTask(context, tasks)

            onSaved() // ← wywoływane na głównym wątku, jest OK
        }
    }
}
