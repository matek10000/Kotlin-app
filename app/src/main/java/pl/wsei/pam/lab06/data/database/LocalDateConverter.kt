package pl.wsei.pam.lab06.data.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    companion object {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        fun toMillis(date: LocalDate): Long =
            date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()

        fun fromMillis(millis: Long): LocalDate =
            java.time.Instant.ofEpochMilli(millis).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
    }


    @TypeConverter
    fun fromDate(date: LocalDate): String = date.format(formatter)

    @TypeConverter
    fun toDate(str: String): LocalDate = LocalDate.parse(str, formatter)
}
