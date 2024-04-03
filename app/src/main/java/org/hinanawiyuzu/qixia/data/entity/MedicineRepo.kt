package org.hinanawiyuzu.qixia.data.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate

/**
 * 药品库存信息
 * @param name 药物名称
 * @param remainAmount 剩余数量
 * @param expiryDate 药物到期时间
 * @param frequency 服药频率
 * @param attentionMatter 药物注意事项
 */
@Entity(tableName = "medicine_repo")
data class MedicineRepo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val remainAmount: String,
    @TypeConverters(UriConverter::class)
    val imageUri: Uri,
    @TypeConverters(LocalDateConverter::class)
    val expiryDate: LocalDate,
    @TypeConverters(MedicineFrequencyConverter::class)
    val frequency: MedicineFrequency,
    @TypeConverters(AttentionMatterConverter::class)
    val attentionMatter: AttentionMatter?
)


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
    OnceThreeDays, // 三日一次
    OnceFourDays, // 四日一次
    OnceFiveDays, //五日一次
    OnceSixDays, //六日一次
    OnceAWeek, //一周一次
    OnceTwoWeeks, //两周一次
    OnceThreeWeeks, //三周一次
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
            OnceThreeDays -> "三日一次"
            OnceFourDays -> "四日一次"
            OnceFiveDays -> "五日一次"
            OnceSixDays -> "六日一次"
            OnceAWeek -> "一周一次"
            OnceTwoWeeks -> "两周一次"
            OnceThreeWeeks -> "三周一次"
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
/**
 * 服药注意事项枚举类
 */
enum class AttentionMatter {
    None,
    EmptyStomach, //空腹
    KeepInDarkPlace, //避光
    Desiccation; //干燥

    fun convertToString(): String {
        return when (this) {
            None -> "无"
            EmptyStomach -> "空腹"
            KeepInDarkPlace -> "避光"
            Desiccation -> "干燥"
        }
    }
}

