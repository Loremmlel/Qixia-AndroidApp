package org.hinanawiyuzu.qixia.notification

import android.app.*
import android.content.*
import android.media.*
import android.util.*
import androidx.core.app.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.hinanawiyuzu.qixia.*
import org.hinanawiyuzu.qixia.data.container.*
import java.time.*

enum class NotificationType {
    TAKE_MEDICINE_REMIND;

    fun convertToName(): String {
        return when (this) {
            TAKE_MEDICINE_REMIND -> "服药提醒"
        }
    }
}

/**
 * 创建各种类型通知的类
 * @property context 上下文
 * @property notificationManager 通知管理器
 * @author HinanawiYuzu
 */
class Notification(private val context: Context) {
    private val notificationManager: NotificationManager = context.getSystemService(NotificationManager::class.java)

    /**
     * 创建服药提醒
     * @param content 通知正文
     * @author HinanawiYuzu
     */
    fun createTakeMedicine(content: String, remindId: Int) {
        Log.e("qixia", "createTakeMedicine: $content")
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("targetScreen", "RemindScreen")
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        setNotificationChannel(NotificationType.TAKE_MEDICINE_REMIND)
        val builder = NotificationCompat.Builder(context, NotificationType.TAKE_MEDICINE_REMIND.name)
            .setContentTitle(NotificationType.TAKE_MEDICINE_REMIND.convertToName())
            .setContentText(content)
            .setSmallIcon(R.mipmap.ic_launcher) //是MIUI缓存了旧的图标，所以导致不生效……
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        notificationManager.notify(generateRandomId(NotificationManager.IMPORTANCE_HIGH), builder.build())
        CoroutineScope(Dispatchers.IO).launch {
            val repo = AppOfflineDataContainer(context).alarmDateTimeRepository
            val alarmDateTime = repo.getStreamByRemindId(remindId).firstOrNull()
            alarmDateTime?.let {
                if (it.endDateTime.toLocalDate() == LocalDate.now())
                    repo.delete(it)
            }
        }
    }

    private fun setNotificationChannel(type: NotificationType) {
        if (notificationManager.getNotificationChannel(type.name) != null)
            return
        val takeMedicineChannel: NotificationChannel?
        when (type) {
            NotificationType.TAKE_MEDICINE_REMIND -> {
                takeMedicineChannel = NotificationChannel(
                    type.name,
                    type.convertToName(),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableVibration(true)
                    lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
                    setShowBadge(true)
                    // 设置通知铃声为默认铃声
                    val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    setSound(
                        alarmSound, AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build()
                    )
                }
                notificationManager.createNotificationChannel(takeMedicineChannel)
            }
        }
    }

    private fun generateRandomId(importance: Int): Int {
        return when (importance) {
            NotificationManager.IMPORTANCE_MIN -> (0..Int.MAX_VALUE / 4).random()
            NotificationManager.IMPORTANCE_LOW -> (Int.MAX_VALUE / 4..Int.MAX_VALUE / 2).random()
            NotificationManager.IMPORTANCE_DEFAULT -> (Int.MAX_VALUE / 2..Int.MAX_VALUE / 4 * 3).random()
            NotificationManager.IMPORTANCE_HIGH -> (Int.MAX_VALUE / 4 * 3..Int.MAX_VALUE).random()
            else -> (0..Int.MAX_VALUE).random()
        }
    }
}