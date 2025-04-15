package pl.wsei.pam.lab06.data.repository

import kotlinx.coroutines.flow.*
import pl.wsei.pam.lab06.data.model.Priority
import pl.wsei.pam.lab06.data.model.TodoTask
import java.time.LocalDate

class TodoRepository : TodoTaskRepository {

    private val tasks = MutableStateFlow(
        listOf(
            TodoTask(title = "Programming", deadline = LocalDate.of(2024, 4, 18), isDone = false, priority = Priority.Low),
            TodoTask(title = "Teaching", deadline = LocalDate.of(2024, 5, 12), isDone = false, priority = Priority.High),
            TodoTask(title = "Learning", deadline = LocalDate.of(2024, 6, 28), isDone = true, priority = Priority.Low),
            TodoTask(title = "Cooking", deadline = LocalDate.of(2024, 8, 18), isDone = false, priority = Priority.Medium),
        )
    )

    override fun getAllAsStream(): Flow<List<TodoTask>> = tasks.asStateFlow()

    override fun getItemAsStream(id: Int): Flow<TodoTask?> {
        return tasks.map { it.find { task -> task.id == id } }
    }

    override suspend fun insertItem(item: TodoTask) {
        tasks.value = tasks.value + item
    }

    override suspend fun deleteItem(item: TodoTask) {
        tasks.value = tasks.value - item
    }

    override suspend fun updateItem(item: TodoTask) {
        tasks.value = tasks.value.map {
            if (it.id == item.id) item else it
        }
    }
}
