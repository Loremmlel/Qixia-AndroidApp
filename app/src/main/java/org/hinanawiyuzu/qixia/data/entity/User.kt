package org.hinanawiyuzu.qixia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters


/**
 * 用户基本信息表
 * @param id 自动生成的主键
 * @param name 用户姓名
 * @param phone 用户手机
 * @param password 用户密码
 * @param loginState 用户是否处于已登录状态
 * @param sexual 用户性别
 * @param age 用户年龄
 * @param serialNumber 用户的智能药箱序列码
 * @param medicalHistory 用户病史列表。目前来说是硬编码的病史字符串组的下标。
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phone: String,
    val password: String,
    val loginState: Boolean,
    val sexual: String,
    val age: Int,
    val serialNumber: String?,
    @TypeConverters(MedicalHistoryConverter::class)
    val medicalHistory: List<Int>?
)

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