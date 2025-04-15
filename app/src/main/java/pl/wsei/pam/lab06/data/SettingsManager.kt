package pl.wsei.pam.lab06.data.settings

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)

    fun saveSettings(settings: ReminderSettings) {
        prefs.edit()
            .putInt("daysBefore", settings.daysBefore)
            .putInt("repeatCount", settings.repeatCount)
            .putInt("intervalHours", settings.intervalHours)
            .apply()
    }

    fun loadSettings(): ReminderSettings {
        return ReminderSettings(
            daysBefore = prefs.getInt("daysBefore", 1),
            repeatCount = prefs.getInt("repeatCount", 3),
            intervalHours = prefs.getInt("intervalHours", 4)
        )
    }
}
