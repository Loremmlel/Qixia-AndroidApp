package org.hinanawiyuzu.qixia.data.source.fake

import org.hinanawiyuzu.qixia.data.entity.*

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
  ),
  Traceability(
    traceabilityCode = "84348400008509525767",
    essential = TraceabilityEssential(
      numberOfPeople = 1,
      chineseCommonName = "异维A酸软胶囊",
      chineseTradeName = "泰尔丝",
      standardCode = "86900805000154",
      dosageForm = "中国药典剂型",
      specification = "软胶囊剂 10mg",
      packingSpecification = "20粒/盒",
      packageConversionRatio = "20",
      validity = "24月",
      registrationCertificateNumber = "国药准字H10930210",
      registrationCertificateNumberValidity = "2025-03-08"
    ),
    produce = TraceabilityProduce(
      produceDate = "2023-11-07",
      validityDate = "2025-11-06",
      produceBatch = "02231105"
    ),
    attribute = TraceabilityAttribute(
      registerSort = "化学药",
      essentialDrug = "否",
      prescriptionDrug = "是"
    ),
    manufacturer = TraceabilityManufacturer(
      licenseeName = "上海信谊万象药业股份有限公司",
      licenseeCreditCode = "91310000631358877Y",
      manufacturerName = "上海信谊延安药业有限公司",
      manufacturerCreditCode = "91310116753162119M"
    )
  )
)