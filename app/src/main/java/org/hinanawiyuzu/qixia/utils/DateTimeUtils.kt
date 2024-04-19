package org.hinanawiyuzu.qixia.utils

import java.time.*


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
