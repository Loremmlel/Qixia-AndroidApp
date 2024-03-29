package org.hinanawiyuzu.qixia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户基本信息表
 * @param id 自动生成的主键
 * @param name 用户姓名
 * @param phone 用户手机
 * @param password 用户密码
 * @param medicalHistory 用户病史列表。目前来说是硬编码的病史字符串组的下标。
 */
@Entity(tableName = "user_info")
data class UserInfo (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val password: String,
    val medicalHistory: List<Int>?
)