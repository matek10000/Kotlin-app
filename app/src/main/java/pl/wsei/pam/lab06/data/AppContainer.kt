package pl.wsei.pam.lab06.data

import pl.wsei.pam.lab06.data.repository.TodoTaskRepository

interface AppContainer {
    val todoRepository: TodoTaskRepository
}
