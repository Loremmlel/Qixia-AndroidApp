package org.hinanawiyuzu.qixia.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 药品信息实体类。
 * @param id 编号
 * @param registrationCertificateNumber 注册证号或批准文号
 * @param productName 产品名称
 * @param dosageForm 剂型
 * @param specification 规格
 */
@Entity(tableName = "medicine_info")
data class MedicineInfo(
  // 为什么要都加上可空类型呢。因为我用Python的sqlite3创建数据库的时候，它给所有字段标记的默认都是可空。
  @PrimaryKey
  val id: Int?,
  @ColumnInfo(name = "registration_certificate_number")
  val registrationCertificateNumber: String?,
  @ColumnInfo(name = "product_name")
  val productName: String?,
  @ColumnInfo(name = "dosage_form")
  val dosageForm: String?,
  val specification: String?
)