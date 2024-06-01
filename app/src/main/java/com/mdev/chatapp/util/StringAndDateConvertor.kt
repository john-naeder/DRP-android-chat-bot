package com.mdev.chatapp.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class StringAndDateConvertor {
    companion object {
        fun dateToString(date: LocalDate?): String {
            return if (date != null) {
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                date.format(formatter)
            } else {
                "Unknown"
            }
        }

        fun stringToDate(date: String): LocalDate {
            return if (date.isBlank()) LocalDate.now()
            else LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }

        fun isValidDate(input: String): Boolean {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            dateFormat.isLenient = false
            return try {
                dateFormat.parse(input)
                true
            } catch (e: ParseException) {
                false
            }
        }
    }
}