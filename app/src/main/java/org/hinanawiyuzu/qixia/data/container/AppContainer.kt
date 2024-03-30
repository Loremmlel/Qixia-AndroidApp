package org.hinanawiyuzu.qixia.data.container

import android.content.Context
import org.hinanawiyuzu.qixia.data.database.QixiaDatabase
import org.hinanawiyuzu.qixia.data.repo.OfflineUserInfoRepository
import org.hinanawiyuzu.qixia.data.repo.UserInfoRepository


interface AppContainer {
    val userInfoRepository: UserInfoRepository
}

class AppDataContainer(
    private val context: Context
) : AppContainer {
    override val userInfoRepository: UserInfoRepository by lazy {
        OfflineUserInfoRepository(QixiaDatabase.getDatabase(context).userInfoDao())
    }
}