package com.finpo.app.utils

object TimeFormatter {
    fun formatTime(date: String): String {
        return try {
            val yearMonthTime = date.split("-")
            val dayTime = yearMonthTime[2].split("T")
            val year = yearMonthTime[0]
            val month =
                if (yearMonthTime[1][0] == '0') yearMonthTime[1][1].toString() else yearMonthTime[1]
            val day = if (dayTime[0][0] == '0') dayTime[0][1].toString() else dayTime[0]
            val time = dayTime[1].split(":")
            var hour = time[0].toInt()
            val min = time[1]
            var ampm = ""
            when {
                hour - 12 > 0 -> {
                    ampm = "오후"
                    hour -= 12
                }
                hour == 12 -> {
                    ampm = "오후"
                }
                else -> {
                    ampm = "오전"
                }
            }

            "${year}년 ${month}월 ${day}일 ${ampm} ${hour}:${min}"
        } catch (e: Exception) {""}
    }
}