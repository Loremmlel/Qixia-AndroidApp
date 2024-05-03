package org.hinanawiyuzu.qixia.data.source.fake

import org.hinanawiyuzu.qixia.data.entity.Traceability
import org.hinanawiyuzu.qixia.data.entity.TraceabilityEssential
import org.hinanawiyuzu.qixia.data.entity.TraceabilityManufacturer
import org.hinanawiyuzu.qixia.data.entity.TraceabilityProduce

val fakeTraceabilityInformation: List<Traceability> = listOf(
  Traceability(
    traceabilityCode = "83625280018604070913",
    essential = TraceabilityEssential(
      numberOfPeople = 1,
      chineseCommonName = "草酸艾司西酞普兰片",
      chineseTradeName = "草酸艾司西酞普兰片",
      standardCode = "86904111000129",
      dosageForm = "片剂",
      specification = "5mg(以艾司西酞普兰计)",
      packingSpecification = "40片/盒",
      packageConversionRatio = "40",
      validity = "36月",
      registrationCertificateNumber = "国药准字H20080599",
      registrationCertificateNumberValidity = "2023-05-24"
    ),
    produce = TraceabilityProduce(
      produceDate = "2023-11-03",
      validityDate = "2026-10-31",
      produceBatch = "2311202"
    ),
    attribute = null,
    manufacturer = TraceabilityManufacturer(
      licenseeName = "山东京卫制药有限公司",
      licenseeCreditCode = "91370900613681048D",
      manufacturerName = "山东京卫制药有限公司",
      manufacturerCreditCode = "91370900613681048D"
    )
  )
)