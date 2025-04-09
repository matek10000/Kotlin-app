package pl.wsei.pam.lab01.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

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
