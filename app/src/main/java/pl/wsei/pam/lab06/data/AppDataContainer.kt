package pl.wsei.pam.lab06.data

import android.content.Context
import pl.wsei.pam.lab06.data.database.AppDatabase
import pl.wsei.pam.lab06.data.notifications.NotificationHandler
import pl.wsei.pam.lab06.data.provider.CurrentDateProvider
import pl.wsei.pam.lab06.data.provider.DefaultCurrentDateProvider
import pl.wsei.pam.lab06.data.repository.DatabaseTodoTaskRepository
import pl.wsei.pam.lab06.data.repository.TodoTaskRepository

class AppDataContainer(private val context: Context) : AppContainer {

    private val database by lazy {
        AppDatabase.getInstance(context)
    }

    override val todoRepository: TodoTaskRepository by lazy {
        DatabaseTodoTaskRepository(database.taskDao())
    }

    override val currentDateProvider: CurrentDateProvider = DefaultCurrentDateProvider()

    override val notificationHandler: NotificationHandler = NotificationHandler(context)
}
