package pl.wsei.pam.lab06.data

import android.content.Context
import pl.wsei.pam.lab06.data.repository.TodoRepository
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository

class AppDataContainer(private val context: Context) : AppContainer {
    override val todoRepository: TodoTaskRepository = TodoRepository()
}
