package org.hinanawiyuzu.qixia.data.entity

import android.net.*
import androidx.room.*
import java.time.*
import java.time.format.*

class TakeMethodConverter {
    @TypeConverter
    fun fromString(value: String): TakeMethod {
        return when (value) {
            "饭前" -> TakeMethod.BeforeMeal
            "饭中" -> TakeMethod.AtMeal
            "饭后" -> TakeMethod.AfterMeal
            "任意" -> TakeMethod.NotMatter
            "睡前" -> TakeMethod.BeforeSleep
            else -> throw IllegalArgumentException("无法识别的服药方法")
        }
    }

    @TypeConverter
    fun fromEnum(value: TakeMethod): String {
        return value.convertToString()
    }
}


class MedicalHistoryConverter {
    @TypeConverter
    fun fromList(list: List<Int>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String?): List<Int>? {
        return data?.split(",")?.map { it.toInt() }
    }
}

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDate(string: String): LocalDate {
        return LocalDate.parse(string)
    }
}

class LocalTimeConverter {
    @TypeConverter
    fun fromLocalTime(localTime: LocalTime): String {
        return localTime.toString()
    }

    @TypeConverter
    fun toLocalTime(string: String): LocalTime {
        return LocalTime.parse(string)
    }

    companion object {
        /**
         * 转换为要显示的时间字符串，例如7:  00.
         */
        fun toDisplayString(localTime: LocalTime): String {
            val formatter = DateTimeFormatter.ofPattern("H:  mm")
            return localTime.format(formatter)
        }
    }
}

class LocalDateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime): String {
        return localDateTime.toString()
    }

    @TypeConverter
    fun toLocalDateTime(string: String): LocalDateTime {
        return LocalDateTime.parse(string)
    }
}

class MedicineFrequencyConverter {
    @TypeConverter
    fun fromMedicineFrequency(medicineFrequency: MedicineFrequency): String {
        return medicineFrequency.convertToString()
    }

    @TypeConverter
    fun toMedicineFrequency(string: String): MedicineFrequency {
        return when (string) {
            "一日一次" -> MedicineFrequency.OnceDaily
            "一日两次" -> MedicineFrequency.TwiceDaily
            "一日三次" -> MedicineFrequency.ThreeTimesDaily
            "一日四次" -> MedicineFrequency.FourTimesDaily
            "一日五次" -> MedicineFrequency.FiveTimesDaily
            "一日六次" -> MedicineFrequency.SixTimesDaily
            "两日一次" -> MedicineFrequency.OnceTwoDays
            "三日一次" -> MedicineFrequency.OnceThreeDays
            "四日一次" -> MedicineFrequency.OnceFourDays
            "五日一次" -> MedicineFrequency.OnceFiveDays
            "六日一次" -> MedicineFrequency.OnceSixDays
            "一周一次" -> MedicineFrequency.OnceAWeek
            "两周一次" -> MedicineFrequency.OnceTwoWeeks
            "三周一次" -> MedicineFrequency.OnceThreeWeeks
            "一月一次" -> MedicineFrequency.OnceAMonth
            else -> MedicineFrequency.OnceDaily
        }
    }
}

class AttentionMatterConverter {
    @TypeConverter
    fun fromAttentionMatter(attentionMatter: AttentionMatter): String {
        return attentionMatter.convertToString()
    }

    @TypeConverter
    fun toAttentionMatter(string: String): AttentionMatter {
        return when (string) {
            "无" -> AttentionMatter.None
            "空腹" -> AttentionMatter.EmptyStomach
            "避光" -> AttentionMatter.KeepInDarkPlace
            "干燥" -> AttentionMatter.Desiccation
            else -> AttentionMatter.None
        }
    }
}

class IntListConverter {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(string: String): List<Int> {
        return string.split(",").map { it.toInt() }
    }
}

class UriConverter {
    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(string: String): Uri {
        return Uri.parse(string)
    }
}

class BooleanListConverter {
    @TypeConverter
    fun fromBooleanList(list: List<Boolean>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toBooleanList(string: String): List<Boolean> {
        return string.split(",").map { it.toBoolean() }
    }
}