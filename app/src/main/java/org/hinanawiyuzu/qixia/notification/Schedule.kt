package org.hinanawiyuzu.qixia.notification

import android.annotation.*
import android.app.*
import android.content.*
import android.util.*
import org.hinanawiyuzu.qixia.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.exception.*
import org.hinanawiyuzu.qixia.utils.*

// 一天毫秒数
const val oneDayMillis = 86400000L

class Schedule(private val context: Context) {
    private val alarmManager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    /**
     * 则通过val intent = Intent(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_REQUEST_INTENT);startActivity(intent)
     * 来跳转到授权的页面
     */
    @SuppressLint("ScheduleExactAlarm")
    fun setTakeMedicineAlarm(
        medicineReminds: List<MedicineRemind>,
        requestCodes: List<List<Int>>
    ) {
        // 纠结我半天的收不到广播，完全是因为没有设置精确的闹钟！！！！
        // 安卓我日你先人
        val intervalDays = when (medicineReminds[0].frequency) {
            MedicineFrequency.OnceTwoDays -> 2
            MedicineFrequency.OnceAWeek -> 7
            MedicineFrequency.OnceTwoWeeks -> 14
            MedicineFrequency.OnceAMonth -> 30
            else -> 1
        }
        val pendingIntentList = emptyList<PendingIntent>().toMutableList()
        try {
            medicineReminds.forEachIndexed { index, medicineRemind ->
                repeat(requestCodes[index].size) {
                    val remindContent = "该服药啦!请在${medicineRemind.remindTime}之前服用${medicineRemind.name} " +
                            "${medicineRemind.dose}片，注意${medicineRemind.method.convertToDisplayedString()}服用"
                    val intent = Intent()
                        .setPackage(context.packageName)
                        .setComponent(ComponentName(context, TakeMedicineReceiver::class.java))
                        .putExtra("takeMedicineRemindContent", remindContent)
                        .putExtra("remindId", medicineRemind.id)
                        .setAction(GlobalValues.TAKE_MEDICINE_REMIND)
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        requestCodes[index][it],
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        medicineRemind.startDate.atTime(medicineRemind.remindTime).toEpochMillis()
                                + oneDayMillis * intervalDays * it,
                        pendingIntent,
                    )
                    Log.e(
                        "qixia", "setTakeMedicineAlarm:" +
                                "${
                                    (medicineRemind.startDate.atTime(medicineRemind.remindTime).toEpochMillis()
                                            + oneDayMillis * intervalDays * it).toLocalDateTime()
                                }"
                    )
                    pendingIntentList.add(pendingIntent)
                }
            }
        } catch (e: Exception) {
            pendingIntentList.forEach {
                alarmManager.cancel(it)
            }
            throw AlarmSetFailedException()
        }
//        val pendingIntentList: MutableList<PendingIntent> = emptyList<PendingIntent>().toMutableList()
//        try {
//            repeat(days) {
//                val intent = Intent()
//                    .setPackage(context.packageName)
//                    .setComponent(ComponentName(context, TakeMedicineReceiver::class.java))
//                    .putExtra("takeMedicineRemindContent", remindContent)
//                    .putExtra("remindId", remindId)
//                    .setAction(GlobalValues.TAKE_MEDICINE_REMIND)
//                val pendingIntent = PendingIntent.getBroadcast(
//                    context,
//                    requestCodes[it],
//                    intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                )
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    startTimeInMillis + oneDayMillis * it,
//                    pendingIntent
//                )
//                pendingIntentList.add(pendingIntent)
//                Log.d("qixia", "setTakeMedicineAlarm: ${(startTimeInMillis + oneDayMillis * it).toLocalDateTime()}")
//            }
//            Log.d("qixia", "setTakeMedicineAlarm: $days")
//        } catch (e: Exception) {
//            // 用于出现异常时取消已设置的闹钟
//            pendingIntentList.forEach {
//                alarmManager.cancel(it)
//            }
//            throw AlarmSetFailedException()
//        }
    }
}