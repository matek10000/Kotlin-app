package pl.wsei.pam.lab06.data

import androidx.compose.runtime.mutableStateListOf
import java.time.LocalDate

object TodoRepository {
    private val taskList = mutableStateListOf<TodoTask>(
        TodoTask("Programming", LocalDate.of(2024, 4, 18), false, Priority.Low),
        TodoTask("Teaching", LocalDate.of(2024, 5, 12), false, Priority.High),
        TodoTask("Learning", LocalDate.of(2024, 6, 28), true, Priority.Low),
        TodoTask("Cooking", LocalDate.of(2024, 8, 18), false, Priority.Medium),
    )

    fun getTasks(): List<TodoTask> = taskList

    fun addTask(task: TodoTask) {
        taskList.add(task)
    }
}
