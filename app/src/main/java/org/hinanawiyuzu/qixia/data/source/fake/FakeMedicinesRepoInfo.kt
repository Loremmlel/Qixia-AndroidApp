package org.hinanawiyuzu.qixia.data.source.fake

import org.hinanawiyuzu.qixia.data.entity.AttentionMatter
import org.hinanawiyuzu.qixia.data.entity.MedicineFrequency
import org.hinanawiyuzu.qixia.data.entity.MedicineRepo
import java.time.LocalDate

val fakeMedicinesRepoInfo: List<MedicineRepo> = listOf(
    MedicineRepo(
        name = "泮托拉唑钠肠溶片",
        remainAmount = "10片",
        expiryDate = LocalDate.of(2026, 6, 30),
        frequency = MedicineFrequency.OnceDaily,
        attentionMatter = AttentionMatter.EmptyStomach
    ),
    MedicineRepo(
        name = "硫酸氢氯吡咯雷片",
        remainAmount = "10片",
        expiryDate = LocalDate.of(2024, 1, 31),
        frequency = MedicineFrequency.OnceDaily,
        attentionMatter = AttentionMatter.None
    ),
    MedicineRepo(
        name = "草酸艾司西酞普兰片",
        remainAmount = "60片",
        expiryDate = LocalDate.of(2025, 6, 25),
        frequency = MedicineFrequency.OnceDaily,
        attentionMatter = AttentionMatter.None
    )
)