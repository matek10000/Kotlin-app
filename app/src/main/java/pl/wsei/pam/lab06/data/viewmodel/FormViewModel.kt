package pl.wsei.pam.lab06.data.viewmodel

import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.*
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.wsei.pam.lab06.TodoApplication
import pl.wsei.pam.lab06.channelID
import pl.wsei.pam.lab06.data.provider.CurrentDateProvider
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository
import pl.wsei.pam.lab06.data.state.TodoTaskForm
import pl.wsei.pam.lab06.data.state.TodoTaskUiState
import pl.wsei.pam.lab06.data.state.toTodoTask
import pl.wsei.pam.lab06.notificationID
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
            val task = todoTaskUiState.todoTask.toTodoTask()
            repository.insertItem(task)

            val tasks = repository.getAllNow()

            // Sprawdź, czy deadline == jutro
            val deadlineDate = task.deadline
            val today = dateProvider.currentDate
            if (deadlineDate == today.plusDays(1)) {
                // natychmiastowe powiadomienie
                val notification = NotificationCompat.Builder(context, channelID)
                    .setContentTitle("Deadline")
                    .setContentText("Zadanie \"${task.title}\" ma termin jutro!")
                    .setSmallIcon(pl.wsei.pam.lab01.R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setAutoCancel(true)
                    .build()

                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(notificationID, notification)
            }

            updateAlarmForNearestTask(context, tasks)

            onSaved()
        }
    }

}
