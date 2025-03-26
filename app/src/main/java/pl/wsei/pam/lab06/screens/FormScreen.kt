package pl.wsei.pam.lab06.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.wsei.pam.lab06.components.AppTopBar
import pl.wsei.pam.lab06.data.Priority
import pl.wsei.pam.lab06.data.TodoRepository
import pl.wsei.pam.lab06.data.TodoTask
import java.time.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(navController: NavController) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var isDone by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf(Priority.Medium) }

    val dateState = rememberDatePickerState()
    val openDatePicker = remember { mutableStateOf(false) }

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    if (openDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { openDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    openDatePicker.value = false
                    dateState.selectedDateMillis?.let {
                        selectedDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                title = "Formularz",
                showBackIcon = true,
                route = "list"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tytuł zadania") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Czy wykonane?")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isDone,
                    onCheckedChange = { isDone = it }
                )
            }

            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedPriority.name,
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
                                selectedPriority = priority
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = { openDatePicker.value = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Wybierz datę: ${selectedDate.format(DateTimeFormatter.ISO_DATE)}")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val newTask = TodoTask(
                        title = title,
                        deadline = selectedDate,
                        isDone = isDone,
                        priority = selectedPriority
                    )
                    TodoRepository.addTask(newTask)
                    Toast.makeText(context, "Zapisano zadanie", Toast.LENGTH_SHORT).show()
                    navController.navigate("list")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zapisz zadanie")
            }

        }
    }
}
