package pl.wsei.pam.lab06.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import pl.wsei.pam.lab06.*
import pl.wsei.pam.lab06.data.model.TodoTask
import pl.wsei.pam.lab06.data.settings.SettingsManager
import java.time.ZoneId

fun updateAlarmForNearestTask(context: Context, tasks: List<TodoTask>) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    cancelAlarm(context)

    val nearest = tasks
        .filter { !it.isDone }
        .sortedBy { it.deadline }
        .firstOrNull() ?: return

    val settingsManager = SettingsManager(context)
    val settings = settingsManager.loadSettings()

    val alarmTime = nearest.deadline
        .minusDays(settings.daysBefore.toLong())
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
        putExtra(titleExtra, "Deadline")
        putExtra(messageExtra, "Zadanie \"${nearest.title}\" ma termin jutro!")
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        notificationID,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        alarmTime,
        settings.intervalHours * 60 * 60 * 1000L,
        pendingIntent
    )
}


fun cancelAlarm(context: Context) {
    val intent = Intent(context, NotificationBroadcastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        notificationID,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
    )
    pendingIntent?.let {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(it)
    }
}
