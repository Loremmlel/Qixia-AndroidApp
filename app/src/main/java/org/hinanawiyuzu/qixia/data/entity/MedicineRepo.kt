package org.hinanawiyuzu.qixia.data.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate

/**
 * 药品库存信息
 * @param id 编号
 * @param name 药物名称
 * @param dosageForm 剂型
 * @param specification 规格
 * @param imageUri 药物图片Uri
 * @param remainAmount 剩余数量
 * @param expiryDate 药物到期时间
 * @param attentionMatter 药物注意事项
 */
@Entity(tableName = "medicine_repo")
data class MedicineRepo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dosageForm: String? = null,
    val specification: String? = null,
    val remainAmount: String,
    @TypeConverters(UriConverter::class)
    val imageUri: Uri,
    @TypeConverters(LocalDateConverter::class)
    val expiryDate: LocalDate,
    @TypeConverters(AttentionMatterConverter::class)
    val attentionMatter: AttentionMatter?
)


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

