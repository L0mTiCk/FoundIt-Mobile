package com.l0mtick.founditmobile.main.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimeAgo(
    timestampMillis: Long,
    locale: Locale = Locale.getDefault()
): String {
    val now = System.currentTimeMillis()
    val diffMillis = now - timestampMillis

    val seconds = diffMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> {
            if (locale.language == "ru") "Размещено только что"
            else "Posted just now"
        }
        minutes < 60 -> {
            val count = minutes.toInt()
            if (locale.language == "ru") "Размещено ${pluralRu(count, "минуту", "минуты", "минут")} назад"
            else "Posted ${count}m ago"
        }
        hours < 24 -> {
            val count = hours.toInt()
            if (locale.language == "ru") "Размещено ${pluralRu(count, "час", "часа", "часов")} назад"
            else "Posted ${count}h ago"
        }
        days < 7 -> {
            val count = days.toInt()
            if (locale.language == "ru") "Размещено ${pluralRu(count, "день", "дня", "дней")} назад"
            else "Posted ${count}d ago"
        }
        else -> {
            val date = Date(timestampMillis)
            val format = if (locale.language == "ru") SimpleDateFormat("dd.MM.yyyy", locale)
            else SimpleDateFormat("MMM dd, yyyy", locale)
            if (locale.language == "ru") "Размещено ${format.format(date)}"
            else "Posted ${format.format(date)}"
        }
    }
}

fun pluralRu(number: Int, one: String, few: String, many: String): String {
    val mod10 = number % 10
    val mod100 = number % 100
    return when {
        mod10 == 1 && mod100 != 11 -> "$number $one"
        mod10 in 2..4 && (mod100 !in 12..14) -> "$number $few"
        else -> "$number $many"
    }
}
