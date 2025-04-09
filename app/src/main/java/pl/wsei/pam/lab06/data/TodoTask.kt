package pl.wsei.pam.lab01.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "tasks")
data class TodoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val deadline: LocalDate = LocalDate.now(),
    val isDone: Boolean = false,
    val priority: Priority = Priority.Low
)

enum class Priority { High, Medium, Low }
