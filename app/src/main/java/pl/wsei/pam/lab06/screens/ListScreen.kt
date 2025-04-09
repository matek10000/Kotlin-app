package pl.wsei.pam.lab06.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.wsei.pam.lab06.TodoApplication
import pl.wsei.pam.lab06.components.AppTopBar
import pl.wsei.pam.lab06.data.TodoListItem
import pl.wsei.pam.lab06.data.TodoTask

@Composable
fun ListScreen(navController: NavController) {
    val context = LocalContext.current
    val todoRepository = (context.applicationContext as TodoApplication).container.todoRepository
    val tasks: List<TodoTask> = todoRepository.getTasks()

    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                title = "Lista",
                showBackIcon = false,
                route = "form"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("form") },
                content = {
                    Icon(Icons.Filled.Add, contentDescription = "Add task")
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(tasks) { task ->
                TodoListItem(item = task)
            }
        }
    }
}
