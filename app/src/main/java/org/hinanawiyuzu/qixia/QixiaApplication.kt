package org.hinanawiyuzu.qixia

import android.app.Application
import org.hinanawiyuzu.qixia.data.container.AppContainer
import org.hinanawiyuzu.qixia.data.container.AppOfflineDataContainer

class QixiaApplication : Application() {
    lateinit var container: AppContainer
    var currentLoginUserId: Int? = null
    override fun onCreate() {
        super.onCreate()
        container = AppOfflineDataContainer(this)
    }
}