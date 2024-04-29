package org.hinanawiyuzu.qixia.data.entity

import java.time.*

data class FamilyRemind(
    val relationShip: RelationShip,
    val name: String,
    val time: LocalDateTime,
    val medicineName: String
)

enum class RelationShip {
    Father,
    Mother,
    Husband,
    Wife,
    Son,
    Daughter,
    PaternalGrandfather,
    PaternalGrandmother,
    MaternalGrandfather,
    MaternalGrandmother,
    YoungerBrother,
    ElderBrother,
    YoungerSister,
    ElderSister,
    PaternalGrandson,
    PaternalGranddaughter,
    MaternalGrandson,
    MaternalGranddaughter,
    PaternalYoungerUncle,
    PaternalElderUncle,
    PaternalAunt,
    MaternalUncle,
    MaternalAunt,
    PaternalNephew,
    MaternalNephew,
    PaternalNiece,
    MaternalNiece,
    PaternalYoungerMaleCousin,
    PaternalElderMaleCousin,
    PaternalYoungerFemaleCousin,
    PaternalElderFemaleCousin,
    MaternalYoungerMaleCousin,
    MaternalElderMaleCousin,
    MaternalYoungerFemaleCousin,
    MaternalElderFemaleCousin;

    fun toChinese(): String {
        return when (this) {
            Father -> "父亲"
            Mother -> "母亲"
            Husband -> "丈夫"
            Wife -> "妻子"
            Son -> "儿子"
            Daughter -> "女儿"
            PaternalGrandfather -> "祖父"
            PaternalGrandmother -> "祖母"
            MaternalGrandfather -> "外祖父"
            MaternalGrandmother -> "外祖母"
            YoungerBrother -> "弟弟"
            ElderBrother -> "哥哥"
            YoungerSister -> "妹妹"
            ElderSister -> "姐姐"
            PaternalGrandson -> "孙子"
            PaternalGranddaughter -> "孙女"
            MaternalGrandson -> "外孙子"
            MaternalGranddaughter -> "外孙女"
            PaternalYoungerUncle -> "叔叔"
            PaternalElderUncle -> "伯父"
            PaternalAunt -> "姑姑"
            MaternalUncle -> "舅舅"
            MaternalAunt -> "姨妈"
            PaternalNephew -> "侄子"
            PaternalNiece -> "侄女"
            MaternalNephew -> "外甥"
            MaternalNiece -> "外甥女"
            PaternalYoungerMaleCousin -> "堂弟"
            PaternalElderMaleCousin -> "堂哥"
            PaternalYoungerFemaleCousin -> "堂妹"
            PaternalElderFemaleCousin -> "堂姐"
            MaternalYoungerMaleCousin -> "表弟"
            MaternalYoungerFemaleCousin -> "表妹"
            MaternalElderMaleCousin -> "表哥"
            MaternalElderFemaleCousin -> "表姐"
        }
    }
}