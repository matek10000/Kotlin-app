package pl.wsei.pam.lab06.components

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import pl.wsei.pam.lab06.TodoApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavController,
    title: String,
    showBackIcon: Boolean = false,
    route: String? = null,
    onSaveClick: (() -> Unit)? = null
) {
    val context = LocalContext.current.applicationContext as TodoApplication

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

            IconButton(onClick = {
                context.container.notificationHandler.showSimpleNotification()
            }) {
                Icon(imageVector = Icons.Default.Call, contentDescription = "Powiadomienie")
            }
        }
    )
}
