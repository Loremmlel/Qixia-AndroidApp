package org.hinanawiyuzu.qixia.model

/**
 * 服药提醒信息
 * @param time 服药时间
 * @param name 药物名称
 * @param amount 服药数量
 * @param dose 剂量
 * @param method 服药方法（饭前，饭中，饭后，无所谓） -> [TakeMethod]
 * @param isTaken 是否已服用
 */
data class MedicineRemindInfo(
    val time: String,
    val name: String,
    val dose: String,
    val amount: String,
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
    BeforeSleep
}