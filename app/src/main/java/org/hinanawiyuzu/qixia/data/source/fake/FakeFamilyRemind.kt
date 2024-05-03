package org.hinanawiyuzu.qixia.data.source.fake

import org.hinanawiyuzu.qixia.data.entity.FamilyRemind
import org.hinanawiyuzu.qixia.data.entity.RelationShip
import org.hinanawiyuzu.qixia.utils.toLocalDateTime

val fakeFamilyRemind: List<FamilyRemind> = listOf(
  FamilyRemind(
    relationShip = RelationShip.Daughter,
    name = "张三",
    time = "2024-04-29T22:00:00".toLocalDateTime()!!,
    medicineName = "草酸艾司西酞普兰片"
  )
)