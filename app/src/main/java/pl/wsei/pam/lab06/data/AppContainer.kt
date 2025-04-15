package pl.wsei.pam.lab06.data

import pl.wsei.pam.lab06.data.provider.CurrentDateProvider
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository

interface AppContainer {
    val todoRepository: TodoTaskRepository
    val currentDateProvider: CurrentDateProvider
}
