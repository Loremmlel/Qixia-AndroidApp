package org.hinanawiyuzu.qixia.data.source.fake

import org.hinanawiyuzu.qixia.data.entity.MedicineRemind
import org.hinanawiyuzu.qixia.data.entity.TakeMethod
import java.time.LocalDate
import java.time.LocalTime

val fakeMedicinesRemindInfo: List<MedicineRemind> = listOf(
    MedicineRemind(
        remindTime = LocalTime.of(7, 0),
        startDate = LocalDate.of(2021, 10, 1),
        endDate = LocalDate.of(2021, 10, 10),
        name = "泮托拉唑钠肠溶片",
        dose = "40mg",
        amount = "1片",
        method = TakeMethod.BeforeMeal,
        isTaken = true,
        repoId = 5
    ),
    MedicineRemind(
        remindTime = LocalTime.of(8, 0),
        startDate = LocalDate.of(2021, 10, 1),
        endDate = LocalDate.of(2021, 10, 10),
        name = "硫酸氢氯吡咯雷片",
        dose = "75mg",
        amount = "1片",
        method = TakeMethod.AfterMeal,
        isTaken = true,
        repoId = 0
    ),
    MedicineRemind(
        remindTime = LocalTime.of(9, 0),
        startDate = LocalDate.of(2021, 10, 1),
        endDate = LocalDate.of(2021, 10, 10),
        name = "布洛芬缓释胶囊",
        dose = "200mg",
        amount = "1粒",
        method = TakeMethod.AfterMeal,
        isTaken = false,
        repoId = 4
    )
)