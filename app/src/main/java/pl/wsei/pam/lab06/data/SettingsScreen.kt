package pl.wsei.pam.lab06.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.wsei.pam.lab06.data.settings.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val saved = settingsManager.loadSettings()

    var daysBefore by remember { mutableStateOf(saved.daysBefore) }
    var repeatCount by remember { mutableStateOf(saved.repeatCount) }
    var intervalHours by remember { mutableStateOf(saved.intervalHours) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ustawienia powiadomień") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Wstecz")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Ile dni przed terminem powiadomić: $daysBefore")
            Slider(value = daysBefore.toFloat(), onValueChange = { daysBefore = it.toInt() }, valueRange = 1f..7f)

            Text("Liczba powtórzeń powiadomienia: $repeatCount")
            Slider(value = repeatCount.toFloat(), onValueChange = { repeatCount = it.toInt() }, valueRange = 1f..10f)

            Text("Przerwa między powiadomieniami (w godzinach): $intervalHours")
            Slider(value = intervalHours.toFloat(), onValueChange = { intervalHours = it.toInt() }, valueRange = 1f..12f)

            Button(onClick = {
                settingsManager.saveSettings(ReminderSettings(daysBefore, repeatCount, intervalHours))
                navController.popBackStack()
            }) {
                Text("Zapisz ustawienia")
            }
        }
    }
}
