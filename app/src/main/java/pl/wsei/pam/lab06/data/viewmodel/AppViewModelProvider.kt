// pl/wsei/pam/lab06/viewmodel/AppViewModelProvider.kt
package pl.wsei.pam.lab06.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.CreationExtras
import pl.wsei.pam.lab06.TodoApplication
import pl.wsei.pam.lab06.data.viewmodel.FormViewModel
import pl.wsei.pam.lab06.data.viewmodel.ListViewModel

object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val container = todoApplication().container
            FormViewModel(
                repository = container.todoRepository,
                dateProvider = container.currentDateProvider
            )
        }
        initializer {
            ListViewModel(
                repository = todoApplication().container.todoRepository
            )
        }
    }
}


fun CreationExtras.todoApplication(): TodoApplication =
    (this[APPLICATION_KEY] as TodoApplication)
