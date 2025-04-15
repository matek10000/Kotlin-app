package pl.wsei.pam.lab06.data.settings

data class ReminderSettings(
    val daysBefore: Int = 1,
    val repeatCount: Int = 3,
    val intervalHours: Int = 4
)
