package com.l0mtick.founditmobile.main.presentation.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale


fun formatTimeAgo(timestampMillis: Long): String {
    val locale = Locale.getDefault()

    val nowInstant = Instant.now()
    val messageInstant = Instant.ofEpochMilli(timestampMillis)

    val nowDateTime = nowInstant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    val messageDateTime = messageInstant.atZone(ZoneId.systemDefault()).toLocalDateTime()

    val nowDate = nowDateTime.toLocalDate()
    val messageDate = messageDateTime.toLocalDate()

    val diffMillis = nowInstant.toEpochMilli() - messageInstant.toEpochMilli()
    val seconds = diffMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    val daysBetween = ChronoUnit.DAYS.between(messageDate, nowDate)

    return when {
        seconds < 60 -> {
            if (locale.language == "ru") "Только что"
            else "Just now"
        }
        minutes < 60 -> {
            val count = minutes.toInt()
            if (locale.language == "ru") "Размещено ${pluralRu(count, "минуту", "минуты", "минут")} назад"
            else "$count min ago"
        }
        hours < 24 && daysBetween == 0L -> {
            val count = hours.toInt()
            if (locale.language == "ru") "Размещено ${pluralRu(count, "час", "часа", "часов")} назад"
            else "$count hr ago"
        }

        daysBetween == 1L -> {
            if (locale.language == "ru") "Вчера"
            else "Yesterday"
        }
        daysBetween == 2L -> {
            if (locale.language == "ru") "Позавчера"
            else "2 days ago"
        }
        daysBetween < 7L -> {
            val count = daysBetween.toInt()
            if (locale.language == "ru") "${pluralRu(count, "день", "дня", "дней")} назад"
            else "$count days ago"
        }
        messageDate.year == nowDate.year -> {
            val formatter = DateTimeFormatter.ofPattern("d MMMM", locale) // Например, "12 мая"
            if (locale.language == "ru") "Размещено ${formatter.format(messageDateTime)}"
            else "Posted ${formatter.format(messageDateTime)}"
        }
        else -> {
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM) // Например, "12 мая 2023 г."
                .withLocale(locale)
            if (locale.language == "ru") "Размещено ${formatter.format(messageDateTime)}"
            else "Posted ${formatter.format(messageDateTime)}"
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

fun formatRelativeTime(
    timestampMillis: Long?,
    locale: Locale = Locale.getDefault()
): String {
    if (timestampMillis == null) {
        return ""
    }

    val now = System.currentTimeMillis()
    val diff = now - timestampMillis

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> when (locale.language) {
            "ru" -> "только что"
            else -> "just now"
        }

        minutes < 60 -> {
            if (locale.language == "ru") {
                "${pluralRu(minutes.toInt(), "минута", "минуты", "минут")} назад"
            } else {
                "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
            }
        }

        hours < 24 -> {
            if (locale.language == "ru") {
                "${pluralRu(hours.toInt(), "час", "часа", "часов")} назад"
            } else {
                "$hours ${if (hours == 1L) "hour" else "hours"} ago"
            }
        }

        days < 7 -> {
            if (locale.language == "ru") {
                "${pluralRu(days.toInt(), "день", "дня", "дней")} назад"
            } else {
                "$days ${if (days == 1L) "day" else "days"} ago"
            }
        }

        else -> {
            val date = Date(timestampMillis)
            val formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale)
            formatter.format(date)
        }
    }
}

fun formatTimestampToShortDate(millis: Long, locale: Locale = Locale.getDefault()): String {
    val date = Date(millis)
    val format = SimpleDateFormat("MMM dd, yyyy", locale)
    return format.format(date)
}

fun formatChatTimestamp(timestampMillis: Long): String {
    val locale = Locale.getDefault()
    val messageDateTime = Instant.ofEpochMilli(timestampMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()


    val today = LocalDate.now(ZoneId.systemDefault())
    val messageDate = messageDateTime.toLocalDate()

    return when {
        messageDate.isEqual(today) -> {
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(locale)
                .format(messageDateTime)
        }
        messageDate.year == today.year -> {
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
                .withLocale(locale)
                .format(messageDateTime)
        }
        else -> {
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(locale)
                .format(messageDateTime)
        }
    }
}
