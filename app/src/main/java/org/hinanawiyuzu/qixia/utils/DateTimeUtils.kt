package org.hinanawiyuzu.qixia.utils

import org.hinanawiyuzu.qixia.data.entity.MedicineFrequency
import java.time.*
import java.time.temporal.ChronoUnit


/**
 * 将时间戳转换为系统当前时区日期时间。
 */
fun Long.toLocalDateTime(): LocalDateTime {
  return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}


/**
 * 将毫秒时间戳转换为系统当前时区日期。
 */
fun Long.toLocalDate(): LocalDate {
  return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}

/**
 * 将日期时间转换为毫秒时间戳。
 */
fun LocalDateTime.toEpochMillis(): Long {
  return this.toInstant(ZoneId.systemDefault().toZoneOffset()).toEpochMilli()
}

fun ZoneId.toZoneOffset(): ZoneOffset {
  return this.rules.getOffset(Instant.now())
}

/**
 * 计算从指定日期到特定日期为止已经服药的次数。
 */
fun LocalDate.numberOfMedicineTakenUntilSpecificDate(frequency: MedicineFrequency, specificDate: LocalDate): Int {
  val internalDays = when (frequency) {
    MedicineFrequency.OnceTwoDays -> 2
    MedicineFrequency.OnceAWeek -> 7
    MedicineFrequency.OnceTwoWeeks -> 14
    MedicineFrequency.OnceAMonth -> 30
    else -> 1
  }
  return ChronoUnit.DAYS.between(this, specificDate).toInt() / internalDays + 1
}

fun String.toLocalDate(): LocalDate? {
  return try {
    LocalDate.parse(this)
  } catch (e: Exception) {
    null
  }
}

fun String.toLocalDateTime(): LocalDateTime? {
  return try {
    LocalDateTime.parse(this)
  } catch (e: Exception) {
    null
  }
}

/**
 * 计算两个日期时间之间的时间间隔。
 *
 * 以中文的方式返回，如：1天前、2小时后、3分钟前。
 * @param other 另一个日期时间。
 */
fun LocalDateTime.ofChineseIntervalTime(other: LocalDateTime): String {
  val isAfter = this.isAfter(other)
  val days = ChronoUnit.DAYS.between(this.toLocalDate(), other.toLocalDate())
  val hours = ChronoUnit.HOURS.between(this, other)
  val minutes = ChronoUnit.MINUTES.between(this, other)
  var result = when {
    days > 0 -> "${days}天"
    hours > 0 -> "${hours}小时"
    minutes > 0 -> "${minutes}分钟"
    else -> "刚刚"
  }
  result += if (isAfter) "后" else "前"
  return result
}

fun LocalDateTime.toDisplayChinese(): String {
  return "${this.monthValue}.${this.dayOfMonth}  ${this.hour}:${String.format("%02d", this.minute)}分"
}