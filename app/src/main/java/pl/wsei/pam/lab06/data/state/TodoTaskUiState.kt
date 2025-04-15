package pl.wsei.pam.lab06.data.state

import pl.wsei.pam.lab06.data.database.LocalDateConverter
import pl.wsei.pam.lab06.data.model.Priority
import pl.wsei.pam.lab06.data.model.TodoTask
import java.time.LocalDate

data class TodoTaskUiState(
    val todoTask: TodoTaskForm = TodoTaskForm(),
    val isValid: Boolean = false
)

data class TodoTaskForm(
    val id: Int = 0,
    val title: String = "",
    val deadline: Long = LocalDateConverter.toMillis(LocalDate.now()),
    val isDone: Boolean = false,
    val priority: String = Priority.Low.name
)

fun TodoTaskForm.toTodoTask(): TodoTask = TodoTask(
    id = id,
    title = title,
    deadline = LocalDateConverter.fromMillis(deadline),
    isDone = isDone,
    priority = Priority.valueOf(priority)
)
