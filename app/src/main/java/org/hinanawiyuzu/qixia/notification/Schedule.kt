package org.hinanawiyuzu.qixia.notification

import android.annotation.*
import android.app.*
import android.content.*
import android.util.*
import kotlinx.coroutines.*
import org.hinanawiyuzu.qixia.GlobalValues.TAKE_MEDICINE_REMIND
import org.hinanawiyuzu.qixia.preferences.*
import java.time.*

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
    fun setTakeMedicineAlarm(remindContent: String, startTimeInMillis: Long, days: Int) {
        // 纠结我半天的收不到广播，完全是因为没有设置精确的闹钟！！！！
        // 安卓我日你先人
        repeat(days) {
            val intent = Intent()
                .setPackage(context.packageName)
                .setComponent(ComponentName(context, TakeMedicineReceiver::class.java))
                .putExtra("takeMedicineRemindContent", remindContent)
                // 为了区分不同的intent,如果不做差异化处理的话，会被认为是同一个闹钟。最后一个设置的会覆盖前一个的。
                .putExtra("days", it + 1)
                .setAction(TAKE_MEDICINE_REMIND)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                (0..Int.MAX_VALUE).random(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                startTimeInMillis + oneDayMillis * it,
                pendingIntent
            )
            Log.d(
                "qixia", "setTakeMedicineAlarm: ${
                    Instant
                        .ofEpochMilli(startTimeInMillis + oneDayMillis * it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }"
            )
        }
        Log.d("qixia", "setTakeMedicineAlarm: $days")
        val alarmPreferences = AlarmPreferences(context, remindContent)
        CoroutineScope(Dispatchers.IO).launch {
            alarmPreferences.set(days)
        }
    }
}