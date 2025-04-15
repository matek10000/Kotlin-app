package pl.wsei.pam.lab06.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
    onSaveClick: (() -> Unit)? = null // ← DODANE
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackIcon && route != null) {
                IconButton(onClick = { navController.navigate(route) }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (onSaveClick != null) {
                IconButton(onClick = onSaveClick) {
                    Icon(Icons.Filled.Check, contentDescription = "Zapisz")
                }
            }
        }
    )
}
