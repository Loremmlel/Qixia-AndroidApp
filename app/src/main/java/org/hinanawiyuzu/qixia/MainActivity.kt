package org.hinanawiyuzu.qixia

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.hinanawiyuzu.qixia.preferences.PermissionRequestPreferences
import org.hinanawiyuzu.qixia.ui.screen.WelcomeScreen
import org.hinanawiyuzu.qixia.ui.theme.QixiaTheme

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