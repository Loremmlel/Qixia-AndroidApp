package org.hinanawiyuzu.qixia.data.source.fake

import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.utils.*

val fakeFamilyRemind: List<FamilyRemind> = listOf(
    FamilyRemind(
        relationShip = RelationShip.Daughter,
        name = "张三",
        time = "2024-04-29T22:00:00".toLocalDateTime()!!,
        medicineName = "草酸艾司西酞普兰片"
    )
)