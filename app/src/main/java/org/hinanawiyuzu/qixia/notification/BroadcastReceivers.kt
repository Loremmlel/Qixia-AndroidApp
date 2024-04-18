package org.hinanawiyuzu.qixia.notification

import android.app.*
import android.content.*
import android.util.*
import kotlinx.coroutines.*
import org.hinanawiyuzu.qixia.preferences.*

class TakeMedicineReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("qixia", "onReceive: TakeMedicineReceiver")
        val remindContent = intent.getStringExtra("takeMedicineRemindContent") ?: "您有一条服药提醒"
        val notification = Notification(context)
        notification.createTakeMedicine(remindContent)
        CoroutineScope(Dispatchers.IO).launch {
            val alarmPreferences = AlarmPreferences(context, remindContent)
            val currentCount = alarmPreferences.read()
            if (currentCount == 0 || currentCount == null) {
                cancelAlarm(context, intent)
            } else
                alarmPreferences.decrement()
            Log.d("qixia", "onReceive: $currentCount")
        }
    }

    private fun cancelAlarm(context: Context, intent: Intent) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
}