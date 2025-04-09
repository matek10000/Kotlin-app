package pl.wsei.pam.lab01.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    companion object {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    }

    @TypeConverter
    fun fromDate(date: LocalDate): String = date.format(formatter)

    @TypeConverter
    fun toDate(str: String): LocalDate = LocalDate.parse(str, formatter)
}
