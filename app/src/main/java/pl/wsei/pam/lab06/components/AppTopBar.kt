package pl.wsei.pam.lab06.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavController,
    title: String,
    showBackIcon: Boolean = false,
    route: String? = null,
    onSaveClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackIcon && route != null) {
                IconButton(onClick = { navController.navigate(route) }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Wstecz")
                }
            } else if (navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Wstecz")
                }
            }
        },
        actions = {
            if (onSaveClick != null) {
                IconButton(onClick = onSaveClick) {
                    Icon(Icons.Filled.Check, contentDescription = "Zapisz")
                }
            } else {
                IconButton(onClick = {
                    navController.navigate("settings")
                }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Ustawienia")
                }
            }
        }
    )
}
