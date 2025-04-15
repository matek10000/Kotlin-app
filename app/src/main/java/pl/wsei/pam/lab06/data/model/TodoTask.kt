package pl.wsei.pam.lab06.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "tasks")
data class TodoTask(
    @PrimaryKey val id: Int = nextId++,
    val title: String,
    val deadline: LocalDate,
    val isDone: Boolean,
    val priority: Priority
) {
    companion object {
        private var nextId = 1
    }
}

enum class Priority {
    High, Medium, Low
}
