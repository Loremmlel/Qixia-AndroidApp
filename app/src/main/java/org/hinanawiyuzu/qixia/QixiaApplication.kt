package org.hinanawiyuzu.qixia

import android.app.*
import org.hinanawiyuzu.qixia.data.container.*

class QixiaApplication : Application() {
    lateinit var container: AppContainer
    var currentLoginUserId: Int? = null
    override fun onCreate() {
        super.onCreate()
        container = AppOfflineDataContainer(this)
    }
}