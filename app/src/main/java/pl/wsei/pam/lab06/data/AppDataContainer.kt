// pl/wsei/pam/lab06/data/AppDataContainer.kt
package pl.wsei.pam.lab06.data

import android.content.Context

class AppDataContainer(private val context: Context) : AppContainer {
    override val todoRepository: TodoRepository = TodoRepository()
}
