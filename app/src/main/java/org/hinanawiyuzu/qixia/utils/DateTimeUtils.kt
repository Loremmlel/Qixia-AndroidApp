package org.hinanawiyuzu.qixia.utils

import org.hinanawiyuzu.qixia.data.entity.*
import java.time.*
import java.time.temporal.*


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
 * 计算从指定日期到现在为止已经服药的次数。
 */
fun LocalDate.numberOfMedicineTakenUntilNow(frequency: MedicineFrequency): Int {
    val internalDays = when (frequency) {
        MedicineFrequency.OnceTwoDays -> 2
        MedicineFrequency.OnceAWeek -> 7
        MedicineFrequency.OnceTwoWeeks -> 14
        MedicineFrequency.OnceAMonth -> 30
        else -> 1
    }
    return ChronoUnit.DAYS.between(this, LocalDate.now()).toInt() / internalDays + 1
}
