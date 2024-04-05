package org.hinanawiyuzu.qixia.data.source.fake

import android.net.Uri
import org.hinanawiyuzu.qixia.data.entity.AttentionMatter
import org.hinanawiyuzu.qixia.data.entity.MedicineRepo
import java.time.LocalDate

val fakeMedicinesRepoInfo: List<MedicineRepo> = listOf(
    MedicineRepo(
        name = "泮托拉唑钠肠溶片",
        remainAmount = "10片",
        expiryDate = LocalDate.of(2026, 6, 30),
        attentionMatter = AttentionMatter.EmptyStomach,
        imageUri = Uri.parse(""),
    ),
    MedicineRepo(
        name = "硫酸氢氯吡咯雷片",
        remainAmount = "10片",
        expiryDate = LocalDate.of(2024, 1, 31),
        attentionMatter = AttentionMatter.None,
        imageUri = Uri.parse(""),
    ),
    MedicineRepo(
        name = "草酸艾司西酞普兰片",
        remainAmount = "60片",
        expiryDate = LocalDate.of(2025, 6, 25),
        attentionMatter = AttentionMatter.None,
        imageUri = Uri.parse(""),
    )
)