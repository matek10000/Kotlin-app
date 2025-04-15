package pl.wsei.pam.lab06.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.wsei.pam.lab06.data.model.TodoTask

@Dao
interface TodoTaskDao {
    @Insert
    suspend fun insertAll(vararg tasks: TodoTask)

    @Update
    suspend fun update(task: TodoTask)

    @Delete
    suspend fun delete(task: TodoTask)

    @Query("SELECT * FROM tasks ORDER BY deadline DESC")
    fun findAll(): Flow<List<TodoTask>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun find(id: Int): Flow<TodoTask>
}
