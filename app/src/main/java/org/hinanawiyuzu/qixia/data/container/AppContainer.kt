package org.hinanawiyuzu.qixia.data.container

import android.content.Context
import org.hinanawiyuzu.qixia.data.database.QixiaDatabase
import org.hinanawiyuzu.qixia.data.repo.MedicineRemindRepository
import org.hinanawiyuzu.qixia.data.repo.MedicineRepoRepository
import org.hinanawiyuzu.qixia.data.repo.OfflineMedicineRemindRepository
import org.hinanawiyuzu.qixia.data.repo.OfflineMedicineRepoRepository
import org.hinanawiyuzu.qixia.data.repo.OfflineUserRepository
import org.hinanawiyuzu.qixia.data.repo.UserRepository


interface AppContainer {
    val userRepository: UserRepository
    val medicineRepoRepository: MedicineRepoRepository
    val medicineRemindRepository: MedicineRemindRepository
}

class AppDataContainer(
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
}