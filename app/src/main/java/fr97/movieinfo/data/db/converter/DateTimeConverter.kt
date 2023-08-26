package fr97.movieinfo.data.db.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class DateTimeConverter {
    @TypeConverter
    fun toDate(timestamp: Long): LocalDate {
        return LocalDate.ofEpochDay(timestamp);
    }

    @TypeConverter
    fun toTimestamp(date: LocalDate): Long {
        return date.toEpochDay()
    }
}