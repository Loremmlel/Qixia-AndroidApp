package org.hinanawiyuzu.qixia.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.hinanawiyuzu.qixia.QixiaApplication

class TakeMedicineReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val application = context.applicationContext as QixiaApplication
        Log.e("qixia", "onReceive: TakeMedicineReceiver")
        val remindContent = intent.getStringExtra("takeMedicineRemindContent") ?: "您有一条服药提醒"
        val remindId = intent.getIntExtra("remindId", -1)
        val alarmUserId = intent.getIntExtra("alarmUserId", -1)
        if (application.currentLoginUserId == alarmUserId) {
            val notification = Notification(context)
            notification.createTakeMedicine(remindContent, remindId)
        }
    }

//    private fun cancelAlarm(context: Context, intent: Intent, requestCode: Int) {
//        val alarmManager = context.getSystemService(AlarmManager::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            requestCode,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        alarmManager.cancel(pendingIntent)
//    }
}