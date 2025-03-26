package pl.wsei.pam.lab06.data

import java.time.LocalDate

fun todoTasks(): List<TodoTask> = listOf(
    TodoTask("Programming", LocalDate.of(2024, 4, 18), false, Priority.Low),
    TodoTask("Teaching", LocalDate.of(2024, 5, 12), false, Priority.High),
    TodoTask("Learning", LocalDate.of(2024, 6, 28), true, Priority.Low),
    TodoTask("Cooking", LocalDate.of(2024, 8, 18), false, Priority.Medium),
)

enum class Priority { High, Medium, Low }

data class TodoTask(
    val title: String,
    val deadline: LocalDate,
    val isDone: Boolean,
    val priority: Priority
)