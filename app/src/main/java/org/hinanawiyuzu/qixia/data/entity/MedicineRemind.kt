package org.hinanawiyuzu.qixia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 * 服药提醒信息
 * @param time 服药时间
 * @param name 药物名称
 * @param amount 服药数量
 * @param dose 剂量
 * @param method 服药方法（饭前，饭中，饭后，无所谓） -> [TakeMethod]
 * @param isTaken 是否已服用
 */
@Entity(tableName = "medicine_remind")
data class MedicineRemind(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: String,
    val name: String,
    val dose: String,
    val amount: String,
    @TypeConverters(TakeMethodConverter::class)
    val method: TakeMethod,
    val isTaken: Boolean
)

/**
 * 药物服用方法枚举类
 * @property BeforeMeal 饭前
 * @property AtMeal 饭中
 * @property AfterMeal 饭后
 * @property NotMatter 任意
 * @property BeforeSleep 睡前
 */
enum class TakeMethod {
    BeforeMeal,
    AtMeal,
    AfterMeal,
    NotMatter,
    BeforeSleep;
    fun convertToString(): String {
        return when (this) {
            BeforeMeal -> "饭前"
            AtMeal -> "饭中"
            AfterMeal -> "饭后"
            NotMatter -> "任意"
            BeforeSleep -> "睡前"
        }
    }
}

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