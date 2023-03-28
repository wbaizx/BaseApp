package com.base.common.util

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/**
y 年
M 月
d 日
h 时 在上午或下午 (1~12)
H 时 在一天中 (0~23)
m 分
s 秒
S 毫秒
E 星期
D 一年中的第几天
F 一月中第几个星期几
w 一年中第几个星期
W 一月中第几个星期
a 上午 / 下午 标记符
 */

const val FORMAT1 = "yyyy.MM.dd HH:mm:ss"
const val FORMAT2 = "yyyy.MM.dd a hh:mm:ss E"

/**
 * Long型 时间戳 转 字符串
 */
fun Long.timeL2S(format: String): String {
    val date = Date(this)
    try {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(date)

    } catch (_: Exception) {
    }
    return "-"
}

/**
 * 字符串 转 时间戳
 */
fun String.timeS2L(format: String): Long {
    try {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = simpleDateFormat.parse(this)
        val timeLong = date?.time
        if (timeLong != null) {
            return timeLong
        }
    } catch (_: Exception) {
    }
    return 0
}

/**
 * 获取给定时间的下一天的起始时间戳
 */
fun getNextDay(time: Long): Long {
    //打印日期Log
    fun dateLog(calendar: Calendar) {
        debugLog(
            "getNextDay", "${calendar.get(Calendar.YEAR)}年" +
                    "${calendar.get(Calendar.MONTH) + 1}月" +
                    "${calendar.get(Calendar.DAY_OF_MONTH)}日" +
                    "${calendar.get(Calendar.HOUR_OF_DAY)}时" +
                    "${calendar.get(Calendar.MINUTE)}分" +
                    "${calendar.get(Calendar.SECOND)}秒" +
                    "${calendar.get(Calendar.MILLISECOND)}毫秒"
        )
    }

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time

    dateLog(calendar)

    //天数+1
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    //重置其他变量
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    dateLog(calendar)

    return calendar.timeInMillis
}

/**
 * Long型 获取星期
 */
fun Long.getWeekCn(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "星期天"
        Calendar.MONDAY -> "星期一"
        Calendar.TUESDAY -> "星期二"
        Calendar.WEDNESDAY -> "星期三"
        Calendar.THURSDAY -> "星期四"
        Calendar.FRIDAY -> "星期五"
        Calendar.SATURDAY -> "星期六"
        else -> "星期*"
    }
}

/**
 * 此方法仿照 DateUtils.isToday(time)
 *
 * 新版时间日期相关api
 * LocalDate
 * LocalTime
 * LocalDateTime
 * Instant
 *
 * DateTimeFormatte（格式化）
 */
fun isToday(time: Long): Boolean {
    val twoMillis = System.currentTimeMillis()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val zoneId = ZoneId.systemDefault()
        val oneInstant = Instant.ofEpochMilli(time)

        val oneLocalDateTime = LocalDateTime.ofInstant(oneInstant, zoneId)
        val twoInstant = Instant.ofEpochMilli(twoMillis)

        val twoLocalDateTime = LocalDateTime.ofInstant(twoInstant, zoneId)

        return oneLocalDateTime.year == twoLocalDateTime.year
                && oneLocalDateTime.monthValue == twoLocalDateTime.monthValue
                && oneLocalDateTime.dayOfMonth == twoLocalDateTime.dayOfMonth

    } else {
        val cal1 = Calendar.getInstance()
        cal1.timeInMillis = time

        val cal2 = Calendar.getInstance()
        cal2.timeInMillis = twoMillis

        return cal1[Calendar.ERA] == cal2[Calendar.ERA]
                && cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
                && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
    }
}