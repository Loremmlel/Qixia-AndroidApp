package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FillPersonalInformationViewModel : ViewModel() {
    private val illnessSize = Illness.illnesses.size
    var screenHeight by mutableIntStateOf(0)
    var maleSelected by mutableStateOf(true)
        private set
    var femaleSelected by mutableStateOf(false)
        private set
    var age by mutableStateOf("")
        private set
    var serialNumber by mutableStateOf("")
        private set
    var illnessCardsClicked  = mutableStateListOf<Boolean>()
        private set
    init {
        illnessCardsClicked.addAll(List(illnessSize){false})
    }
    fun onMaleSelected() {
        if(!maleSelected) {
            maleSelected = true
            femaleSelected = false
        } else
            return
    }

    fun onFemaleSelected() {
        if(!femaleSelected) {
            femaleSelected = true
            maleSelected = false
        }else
            return
    }

    fun onAgeChanged(value: String) {
        age = value
    }

    fun onSerialNumberChanged(value: String) {
        serialNumber = value
    }

    fun onIllnessCardClicked(id: Int) {
        illnessCardsClicked[id] = illnessCardsClicked[id].not()
        // ”其它“和”无“选项与其它选项冲突
        if (id == illnessSize - 1 || id == illnessSize - 2) {
            illnessCardsClicked.fill(false)
            illnessCardsClicked[id] = true
        } else {
            illnessCardsClicked[illnessSize - 1] = false
            illnessCardsClicked[illnessSize - 2] = false
        }
    }
}
// 因为还没学到数据层，所以暂时硬编码一下。
// 还可以为以后做参考不是么。
// 是时候掏出我的《内科学》了，以前想学医买来的书居然也算是起到作用了。
// 我个人感觉还是慢性病才会用到这个，而慢性病多是内科病。
object Illness {
    val illnesses: List<String> = listOf(
        "恶性肿瘤", "慢性肺、气管病", "结核病",
        "心力衰竭", "高血压", "冠心病", "心律失常",
        "胃炎", "消化性溃疡", "克罗恩病", "病毒性肝炎", "肝硬化",
        "慢性肾炎", "慢性肾衰竭", "尿路感染",
        "偏头痛","脑梗死", "脑出血", "癫痫", "阿尔兹海默症",
        "精神分裂症", "抑郁障碍", "焦虑障碍", "双相情感障碍","强迫障碍", "睡眠障碍",
        "贫血", "血友病",
        "甲亢", "甲减", "尿崩症", "库欣综合征", "糖尿病", "肥胖症",
        "干眼病", "青光眼",
        "类风湿关节炎", "系统性红斑狼疮", "干燥综合征", "痛风",
        "其它","无"
    )
}