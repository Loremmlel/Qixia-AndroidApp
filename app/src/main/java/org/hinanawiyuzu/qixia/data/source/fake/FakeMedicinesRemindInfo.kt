package org.hinanawiyuzu.qixia.data.source.fake

import org.hinanawiyuzu.qixia.model.MedicineRemindInfo
import org.hinanawiyuzu.qixia.model.TakeMethod

val fakeMedicinesRemindInfo: List<MedicineRemindInfo> = listOf(
    MedicineRemindInfo(
        time = "7:  00",
        name = "泮托拉唑钠肠溶片",
        dose = "40mg",
        amount = "1片",
        method = TakeMethod.BeforeMeal,
        isTaken = true
    ),
    MedicineRemindInfo(
        time = "8:  00",
        name = "硫酸氢氯吡咯雷片",
        dose = "75mg",
        amount = "1片",
        method = TakeMethod.AfterMeal,
        isTaken = true
    ),
    MedicineRemindInfo(
        time = "9:  00",
        name = "布洛芬缓释胶囊",
        dose = "200mg",
        amount = "1粒",
        method = TakeMethod.AfterMeal,
        isTaken = true
    ),
    MedicineRemindInfo(
        time = "10:30",
        name = "布地奈德",
        dose = "50μg",
        amount = "1喷",
        method = TakeMethod.NotMatter,
        isTaken = false
    ),
    MedicineRemindInfo(
        time = "12:  00",
        name = "阿莫西林胶囊",
        dose = "500mg",
        amount = "1粒",
        method = TakeMethod.AtMeal,
        isTaken = false
    ),
    MedicineRemindInfo(
        time = "18:00",
        name = "氨磺必利",
        dose = "50mg",
        amount = "1粒",
        method = TakeMethod.BeforeMeal,
        isTaken = false
    ),
    MedicineRemindInfo(
        time = "20:30",
        name = "甲钴胺",
        dose = "500μg",
        amount = "1针",
        method = TakeMethod.BeforeSleep,
        isTaken = false
    )
)