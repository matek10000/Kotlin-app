package pl.wsei.pam.lab06.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pl.wsei.pam.lab06.components.AppTopBar
import pl.wsei.pam.lab06.components.TodoListItem
import pl.wsei.pam.lab06.viewmodel.AppViewModelProvider
import pl.wsei.pam.lab06.data.viewmodel.ListViewModel

@Composable
fun ListScreen(
    navController: NavController,
    viewModel: ListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val listUiState by viewModel.listUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

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
            FloatingActionButton(onClick = { navController.navigate("form") }) {
                Icon(Icons.Filled.Add, contentDescription = "Dodaj zadanie")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(items = listUiState.items, key = { it.id }) { task ->
                TodoListItem(
                    item = task,
                    onStatusToggle = { updatedTask ->
                        coroutineScope.launch {
                            viewModel.updateItem(updatedTask)
                        }
                    }
                )
            }
        }
    }
}
