package org.hinanawiyuzu.qixia

import android.content.*
import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import kotlinx.coroutines.*
import org.hinanawiyuzu.qixia.preferences.*
import org.hinanawiyuzu.qixia.ui.screen.*
import org.hinanawiyuzu.qixia.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            if (Build.VERSION.SDK_INT >= 33) {
                val dataStore = PermissionRequestPreferences(
                    this@MainActivity, android.Manifest.permission
                        .POST_NOTIFICATIONS
                )
                if (dataStore.read() == null) {
                    dataStore.init()
                }
            }
            this.cancel()
        }
        setContent {
            QixiaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WelcomeScreen()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        //TODO: 根据intent中的数据跳转到对应的界面，方法为设置AppViewModel中的AppState
    }
}