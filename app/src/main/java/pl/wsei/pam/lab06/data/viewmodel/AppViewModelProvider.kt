package pl.wsei.pam.lab06.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.CreationExtras
import pl.wsei.pam.lab06.TodoApplication
import pl.wsei.pam.lab06.data.viewmodel.FormViewModel
import pl.wsei.pam.lab06.data.viewmodel.ListViewModel

object AppViewModelProvider {
    val Factory: Factory = viewModelFactory {
        initializer {
            val app = (this[APPLICATION_KEY] as Application) as TodoApplication
            FormViewModel(
                repository = app.container.todoRepository,
                dateProvider = app.container.currentDateProvider
            )
        }
        initializer {
            val app = (this[APPLICATION_KEY] as Application) as TodoApplication
            ListViewModel(
                repository = app.container.todoRepository
            )
        }
    }
}
