package org.hinanawiyuzu.qixia.notification

import android.annotation.*
import android.app.*
import android.content.*
import android.util.*
import org.hinanawiyuzu.qixia.*
import org.hinanawiyuzu.qixia.exception.*
import org.hinanawiyuzu.qixia.utils.*

// 一天毫秒数
const val oneDayMillis = 86400000L

class Schedule(private val context: Context) {
    private val alarmManager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    /**
     * @param remindContent 提醒内容
     * @param startTimeInMillis 开始日期的提醒时间戳
     * @param days 提醒持续天数
     * @throws UnsupportedOperationException 需要精确闹钟权限，用try-catch块捕获，如果捕获到了，
     * 则通过val intent = Intent(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_REQUEST_INTENT);startActivity(intent)
     * 来跳转到授权的页面
     */
    @SuppressLint("ScheduleExactAlarm")
    fun setTakeMedicineAlarm(
        remindId: Int,
        remindContent: String,
        startTimeInMillis: Long,
        days: Int,
        requestCodes: List<Int>
    ) {
        // 纠结我半天的收不到广播，完全是因为没有设置精确的闹钟！！！！
        // 安卓我日你先人
        // 用于出现异常时取消已设置的闹钟
        val pendingIntentList: MutableList<PendingIntent> = emptyList<PendingIntent>().toMutableList()
        try {
            repeat(days) {
                val intent = Intent()
                    .setPackage(context.packageName)
                    .setComponent(ComponentName(context, TakeMedicineReceiver::class.java))
                    .putExtra("takeMedicineRemindContent", remindContent)
                    .putExtra("remindId", remindId)
                    .setAction(GlobalValues.TAKE_MEDICINE_REMIND)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCodes[it],
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    startTimeInMillis + oneDayMillis * it,
                    pendingIntent
                )
                pendingIntentList.add(pendingIntent)
                Log.d("qixia", "setTakeMedicineAlarm: ${(startTimeInMillis + oneDayMillis * it).toLocalDateTime()}")
            }
            Log.d("qixia", "setTakeMedicineAlarm: $days")
        } catch (e: Exception) {
            pendingIntentList.forEach {
                alarmManager.cancel(it)
            }
            throw AlarmSetFailedException()
        }
    }
}