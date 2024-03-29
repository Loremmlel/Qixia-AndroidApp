package org.hinanawiyuzu.qixia.model

import java.time.LocalDate

/**
 * 药品库存信息
 * @param name 药物名称
 * @param remainAmount 剩余数量
 * @param expiryDate 药物到期时间
 * @param frequency 服药频率
 * @param attentionMatter 药物注意事项
 */
data class MedicineRepoInfo(
    val name: String,
    val remainAmount: String,
    val expiryDate: LocalDate,
    val frequency: MedicineFrequency,
    val attentionMatter: AttentionMatter
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
    OnceAMonth //一月一次
}

/**
 * 服药注意事项枚举类
 */
enum class AttentionMatter {
    None,
    EmptyStomach, //空腹
    KeepInDarkPlace, //避光
    Desiccation, //干燥
}