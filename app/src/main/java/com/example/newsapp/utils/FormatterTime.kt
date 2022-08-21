package com.example.newsapp.utils

import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun formatterTime(pubDate: String?) : String {
    if (pubDate == null) return ""
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    val date = formatter.parse(pubDate)
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val utcTime = simpleDateFormat.format(date!!)
    return try {
        val time = simpleDateFormat.parse(utcTime)?.time
        val now = System.currentTimeMillis()
            val timeAgo =
                DateUtils.getRelativeTimeSpanString(
                    time!!,
                    now,
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL
                )
            timeAgo.toString()
    } catch (e: ParseException) {
        e.message.toString()
    }
}
//DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_ABBREV_MONTH