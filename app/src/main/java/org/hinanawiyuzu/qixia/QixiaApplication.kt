package org.hinanawiyuzu.qixia

import android.app.Application
import org.hinanawiyuzu.qixia.data.container.AppContainer
import org.hinanawiyuzu.qixia.data.container.AppDataContainer

class QixiaApplication : Application() {
    lateinit var container: AppContainer
    var currentLoginUserId: Int? = null
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}