package org.hinanawiyuzu.qixia.data.container

import android.content.*
import org.hinanawiyuzu.qixia.data.database.*
import org.hinanawiyuzu.qixia.data.repo.*


interface AppContainer {
    val userRepository: UserRepository
    val medicineRepoRepository: MedicineRepoRepository
    val medicineRemindRepository: MedicineRemindRepository
    val medicineInfoRepository: MedicineInfoRepository
    val alarmDateTimeRepository: AlarmDateTimeRepository
}

class AppOfflineDataContainer(
    private val context: Context
) : AppContainer {
    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(QixiaDatabase.getDatabase(context).userInfoDao())
    }
    override val medicineRepoRepository: MedicineRepoRepository by lazy {
        OfflineMedicineRepoRepository(QixiaDatabase.getDatabase(context).medicineRepoDao())
    }
    override val medicineRemindRepository: MedicineRemindRepository by lazy {
        OfflineMedicineRemindRepository(QixiaDatabase.getDatabase(context).medicineRemindDao())
    }
    override val medicineInfoRepository: MedicineInfoRepository by lazy {
        OfflineMedicineInfoRepository(QixiaDatabase.getDatabase(context).medicineInfoDao())
    }
    override val alarmDateTimeRepository: AlarmDateTimeRepository by lazy {
        OfflineAlarmDateTimeRepository(QixiaDatabase.getDatabase(context).alarmDateTimeDao())
    }
}