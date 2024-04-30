package org.hinanawiyuzu.qixia.data.source.fake

import androidx.annotation.DrawableRes
import org.hinanawiyuzu.qixia.R


/**
 * @param month 月份
 * @param title 标题
 * @param principle 养生原则
 * @param matters 注意事项
 * @param diet 饮食良方
 * @param imageResId 图片资源id
 */
data class HealthCalendar(
    val month: Int,
    val title: String,
    val principle: String,
    val matters: String,
    val diet: String,
    @DrawableRes val imageResId: Int
)

val fakeHealthCalendar: List<HealthCalendar> = listOf(
    HealthCalendar(
        month = 5,
        title = "保心消暑",
        principle = "春夏之交心阳旺盛，人易烦躁不安、好发脾气，机体免疫力低下，需重点护心。",
        matters = "南方需注意防热邪，北方适宜出门郊游。心态宜开朗畅怀、安闲自在，切忌暴怒伤心。",
        diet = "宜多食清热利湿的食物。\n如赤小豆、薏苡仁、绿豆、冬瓜、丝瓜、水芹、黑木耳、藕、胡萝卜、西红柿、西瓜、山药等。",
        imageResId = R.drawable.health_calendar_mars
    )
)