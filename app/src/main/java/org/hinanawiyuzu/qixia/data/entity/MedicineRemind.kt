package org.hinanawiyuzu.qixia.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.hinanawiyuzu.qixia.data.entity.TakeMethod.AfterMeal
import org.hinanawiyuzu.qixia.data.entity.TakeMethod.AtMeal
import org.hinanawiyuzu.qixia.data.entity.TakeMethod.BeforeMeal
import org.hinanawiyuzu.qixia.data.entity.TakeMethod.BeforeSleep
import org.hinanawiyuzu.qixia.data.entity.TakeMethod.NotMatter
import java.time.LocalDate
import java.time.LocalTime

/**
 * 服药提醒信息
 * @param id 主键
 * @param remindTime 服药时间
 * @param startDate 开始提醒的日期
 * @param endDate 结束提醒的日期
 * @param name 药物名称
 * @param amount 服药数量
 * @param dose 剂量
 * @param method 服药方法（饭前，饭中，饭后，无所谓） -> [TakeMethod]
 * @param isTaken 是否已服用
 * @param repoId 该提醒对应的药物仓库id
 */
@Entity(
    tableName = "medicine_remind",
    foreignKeys = [
        ForeignKey(
            entity = MedicineRepo::class,
            parentColumns = ["id"],
            childColumns = ["repoId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id"]),
        Index(value = ["repoId"])
    ]
)
data class MedicineRemind(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @TypeConverters(LocalTimeConverter::class)
    val remindTime: LocalTime,
    @TypeConverters(LocalDateConverter::class)
    val startDate: LocalDate,
    @TypeConverters(LocalDateConverter::class)
    val endDate: LocalDate,
    val name: String,
    val dose: String,
    val amount: String,
    @TypeConverters(TakeMethodConverter::class)
    val method: TakeMethod,
    val isTaken: Boolean,
    val repoId: Int,
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

