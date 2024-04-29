package org.hinanawiyuzu.qixia.data.entity

data class Traceability(
    val traceabilityCode: String,
    val essential: TraceabilityEssential,
    val produce: TraceabilityProduce,
    val attribute: TraceabilityAttribute?,
    val manufacturer: TraceabilityManufacturer
)

/**
 * 药品追溯信息之基本信息
 * @param numberOfPeople 总扫码人数
 * @param chineseCommonName 药品通用名称(中文)
 * @param chineseTradeName 药品商品名(中文)
 * @param standardCode 药品本位码
 * @param dosageForm 剂型
 * @param specification 制剂规格
 * @param packingSpecification 包装规格
 * @param packageConversionRatio 包装转换比,这个是什么意思？
 * @param validity 有效期(按月)
 * @param registrationCertificateNumber 注册证号或批准文号
 * @param registrationCertificateNumberValidity 注册证号或批准文号有效期
 */
data class TraceabilityEssential(
    val numberOfPeople: Int,
    val chineseCommonName: String,
    val chineseTradeName: String,
    val standardCode: String,
    val dosageForm: String,
    val specification: String,
    val packingSpecification: String,
    val packageConversionRatio: String,
    val validity: String,
    val registrationCertificateNumber: String,
    val registrationCertificateNumberValidity: String
) {
    fun toDisplayedStringMap(): Map<String, String> {
        return mapOf(
            "药品通用名称(中文)" to chineseCommonName,
            "药品本位码" to standardCode,
            "剂型" to dosageForm,
            "制剂规格" to specification,
            "包装规格" to packingSpecification,
            "包装转换比" to packageConversionRatio,
            "药品有效期" to validity,
            "药品批准文号" to registrationCertificateNumber,
            "药品批准文号有效期" to registrationCertificateNumberValidity
        )
    }
}

/**
 * 药品追溯信息之生产信息
 * @param produceDate 生产日期
 * @param validityDate 有效期至
 * @param produceBatch 生产批号
 */
data class TraceabilityProduce(
    val produceDate: String,
    val validityDate: String,
    val produceBatch: String,
) {
    fun toDisplayedStringMap(): Map<String, String> {
        return mapOf(
            "药品生产日期" to produceDate,
            "药品有效期截止日期" to validityDate,
            "药品生产批号" to produceBatch
        )
    }
}

/**
 * 药品追溯信息之类别属性
 * @param registerSort 药品注册分类
 * @param essentialDrug 是否为基本药物
 * @param prescriptionDrug 是否为处方药
 */
data class TraceabilityAttribute(
    val registerSort: String,
    val essentialDrug: String,
    val prescriptionDrug: String
) {
    fun toDisplayedStringMap(): Map<String, String> {
        return mapOf(
            "药品注册分类" to registerSort,
            "药品基本药物标识" to essentialDrug,
            "处方药标识" to prescriptionDrug
        )
    }

}

/**
 * 药品追溯信息之生产企业信息
 * @param licenseeName 上市许可持有人名称
 * @param licenseeCreditCode 上市许可持有人统一社会信用代码
 * @param manufacturerName 生产企业名称
 * @param manufacturerCreditCode 生产企业统一社会信用代码
 */
data class TraceabilityManufacturer(
    val licenseeName: String,
    val licenseeCreditCode: String,
    val manufacturerName: String,
    val manufacturerCreditCode: String
) {
    fun toDisplayedStringMap(): Map<String, String> {
        return mapOf(
            "上市许可持有人名称" to licenseeName,
            "统一社会信用代码(上市许可持有人)" to licenseeCreditCode,
            "生产企业名称" to manufacturerName,
            "统一社会信用代码(生产企业)" to manufacturerCreditCode
        )
    }
}

