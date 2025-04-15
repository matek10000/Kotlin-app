package pl.wsei.pam.lab06.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wsei.pam.lab06.data.model.TodoTask
import java.time.format.DateTimeFormatter

@Composable
fun TodoListItem(
    item: TodoTask,
    onStatusToggle: (TodoTask) -> Unit,
    modifier: Modifier = Modifier
) {
    val statusColor = if (item.isDone) Color(0xFF2E7D32) else Color(0xFFB71C1C)
    val statusText = if (item.isDone) "✓" else "X"

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xF5F5F9FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Tytuł", fontSize = 12.sp)
                Text(text = "Deadline", fontSize = 12.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = item.title, style = MaterialTheme.typography.titleLarge)
                    Text(text = item.priority.name, fontSize = 14.sp)
                }

                Text(
                    text = item.deadline.format(DateTimeFormatter.ISO_DATE),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(statusColor, shape = CircleShape)
                        .clickable { onStatusToggle(item.copy(isDone = !item.isDone)) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = statusText,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}
