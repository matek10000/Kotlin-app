package pl.wsei.pam.lab06.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import pl.wsei.pam.lab06.data.database.TodoTaskDao
import pl.wsei.pam.lab06.data.model.TodoTask

class DatabaseTodoTaskRepository(
    private val taskDao: TodoTaskDao
) : TodoTaskRepository {

    override fun getAllAsStream(): Flow<List<TodoTask>> =
        taskDao.findAll()

    override fun getItemAsStream(id: Int): Flow<TodoTask?> =
        taskDao.find(id)

    override suspend fun getAllNow(): List<TodoTask> =
        taskDao.findAll().first()

    override suspend fun insertItem(item: TodoTask) {
        taskDao.insertAll(item)
    }

    override suspend fun deleteItem(item: TodoTask) {
        taskDao.delete(item)
    }

    override suspend fun updateItem(item: TodoTask) {
        taskDao.update(item)
    }
}
