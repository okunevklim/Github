package com.example.github.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    fun formatCreatedDate(createdDate: String): String {
        val outputFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
        val inputFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date: Date = inputFormat.parse(createdDate)
        return outputFormat.format(date)
    }
}