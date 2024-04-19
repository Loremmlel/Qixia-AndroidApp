package org.hinanawiyuzu.qixia.notification

import android.app.*
import android.content.*
import android.util.*

class TakeMedicineReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("qixia", "onReceive: TakeMedicineReceiver")
        val remindContent = intent.getStringExtra("takeMedicineRemindContent") ?: "您有一条服药提醒"
        val remindId = intent.getStringExtra("remindId")?.toInt() ?: -1
        val notification = Notification(context)
        notification.createTakeMedicine(remindContent, remindId)
    }

    private fun cancelAlarm(context: Context, intent: Intent, requestCode: Int) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
}