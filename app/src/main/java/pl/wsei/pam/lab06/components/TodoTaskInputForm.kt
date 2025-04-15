package pl.wsei.pam.lab06.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.wsei.pam.lab06.data.state.TodoTaskForm
import java.time.LocalDate
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Alignment
import pl.wsei.pam.lab06.data.model.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTaskInputForm(
    item: TodoTaskForm,
    onValueChange: (TodoTaskForm) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = item.title,
            onValueChange = { onValueChange(item.copy(title = it)) },
            label = { Text("Tytuł zadania") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Wykonane?")
            Switch(
                checked = item.isDone,
                onCheckedChange = { onValueChange(item.copy(isDone = it)) }
            )
        }

        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = item.priority,
                onValueChange = {},
                readOnly = true,
                label = { Text("Priorytet") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Priority.values().forEach { priority ->
                    DropdownMenuItem(
                        text = { Text(priority.name) },
                        onClick = {
                            onValueChange(item.copy(priority = priority.name))
                            expanded = false
                        }
                    )
                }
            }
        }

        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = item.deadline)
        var showDatePicker by remember { mutableStateOf(false) }

        OutlinedButton(onClick = { showDatePicker = true }) {
            Text("Data zakończenia: ${LocalDate.ofEpochDay(item.deadline / (1000 * 60 * 60 * 24))}")
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onValueChange(item.copy(deadline = it))
                        }
                        showDatePicker = false
                    }) {
                        Text("Wybierz")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
