package org.hinanawiyuzu.qixia.data.entity

import androidx.room.*
import org.hinanawiyuzu.qixia.data.entity.IsTake.*
import org.hinanawiyuzu.qixia.data.entity.TakeMethod.*
import org.hinanawiyuzu.qixia.utils.numberOfMedicineTakenUntilSpecificDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/**
 * 服药提醒信息
 * @param id 主键
 * @param remindTime 服药时间
 * @param startDate 开始提醒的日期
 * @param endDate 结束提醒的日期
 * @param name 药物名称
 * @param dose 剂量
 * @param method 服药方法（饭前，饭中，饭后，无所谓） -> [TakeMethod]
 * @param isTaken 是否已服用(是一个布尔数组，长度为从开始日期到结束日期的天数差)
 * @param frequency 服药频率 -> [MedicineFrequency]
 * @param takeTime 用户的实际服药时间
 * @param medicineRepoId 对应的药物仓库id
 * @param userId 设置该提醒的用户id
 */
@Entity(
    tableName = "medicine_remind",
    foreignKeys = [
        ForeignKey(
            entity = MedicineRepo::class,
            parentColumns = ["id"],
            childColumns = ["medicineRepoId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["medicineRepoId"]),
        Index(value = ["userId"])
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
    @TypeConverters(TakeMethodConverter::class)
    val method: TakeMethod,
    @TypeConverters(BooleanListConverter::class)
    val isTaken: List<Boolean>,
    @TypeConverters(MedicineFrequencyConverter::class)
    val frequency: MedicineFrequency,
    @TypeConverters(NullableLocalDateTimeListConverter::class)
    val takeTime: List<LocalDateTime?>,
    val medicineRepoId: Int,
    val userId: Int
)

/**
 * 根据服药频率判断是否要显示在某一天中
 */
fun MedicineRemind.isDisplayedInSpecificDate(specificDate: LocalDate): Boolean {
    val intervalDays = when (frequency) {
        MedicineFrequency.OnceTwoDays -> 2
        MedicineFrequency.OnceAWeek -> 7
        MedicineFrequency.OnceTwoWeeks -> 14
        MedicineFrequency.OnceAMonth -> 30
        else -> 1
    }
    val daysDifference = ChronoUnit.DAYS.between(startDate, specificDate).toInt()
    return daysDifference % intervalDays == 0 && daysDifference >= 0
}

fun MedicineRemind.isTakenInSpecificDate(specificDate: LocalDate): Pair<IsTake, LocalDateTime?> {
    if (!this.isDisplayedInSpecificDate(specificDate) || specificDate !in startDate..endDate)
        return NotNeed to null
    val index = this.startDate.numberOfMedicineTakenUntilSpecificDate(this.frequency, specificDate) - 1
    return if (this.isTaken[index]) Taken to this.takeTime[index] else NotTaken to this.remindTime.atDate(specificDate)
}

/**
 * 用户是否吃了药
 * @property Taken 吃了
 * @property NotTaken 没吃
 * @property NotNeed 不需要吃
 */
enum class IsTake {
    Taken,
    NotTaken,
    NotNeed,
}

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

    fun convertToDisplayedString(): String {
        return when (this) {
            BeforeMeal -> "饭前"
            AtMeal -> "饭中"
            AfterMeal -> "饭后"
            NotMatter -> "任意"
            BeforeSleep -> "睡前"
        }
    }
}


/**
 * 服药频率枚举类
 */
enum class MedicineFrequency {
    OnceDaily, //一日一次
    TwiceDaily, //一日两次
    ThreeTimesDaily, //一日三次
    FourTimesDaily, // 一日四次
    FiveTimesDaily, // 一日五次
    SixTimesDaily, // 一日六次
    OnceTwoDays, // 两日一次
    OnceAWeek, //一周一次
    OnceTwoWeeks, //两周一次
    OnceAMonth; //一月一次

    fun convertToString(): String {
        return when (this) {
            OnceDaily -> "一日一次"
            TwiceDaily -> "一日两次"
            ThreeTimesDaily -> "一日三次"
            FourTimesDaily -> "一日四次"
            FiveTimesDaily -> "一日五次"
            SixTimesDaily -> "一日六次"
            OnceTwoDays -> "两日一次"
            OnceAWeek -> "一周一次"
            OnceTwoWeeks -> "两周一次"
            OnceAMonth -> "一月一次"
        }
    }
}

// 扩展函数太香了！！！
fun String.toMedicineFrequency(): MedicineFrequency {
    return when (this) {
        "一日一次" -> MedicineFrequency.OnceDaily
        "一日两次" -> MedicineFrequency.TwiceDaily
        "一日三次" -> MedicineFrequency.ThreeTimesDaily
        "一日四次" -> MedicineFrequency.FourTimesDaily
        "一日五次" -> MedicineFrequency.FiveTimesDaily
        "一日六次" -> MedicineFrequency.SixTimesDaily
        "两日一次" -> MedicineFrequency.OnceTwoDays
        "一周一次" -> MedicineFrequency.OnceAWeek
        "两周一次" -> MedicineFrequency.OnceTwoWeeks
        "一月一次" -> MedicineFrequency.OnceAMonth
        else -> MedicineFrequency.OnceDaily
    }
}