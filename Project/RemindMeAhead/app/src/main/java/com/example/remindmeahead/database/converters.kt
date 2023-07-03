package com.example.remindmeahead.database

import androidx.room.TypeConverter
import java.sql.Date
import java.text.SimpleDateFormat


class converters {
    private val format = SimpleDateFormat("yyyy-MM-dd")
    @TypeConverter
    fun fromDate(date: Date): String {
        return format.format(date)
    }

    @TypeConverter
    fun toDate(dateString: String): Date {
        return (format.parse(dateString) as Date?)!!
    }
}