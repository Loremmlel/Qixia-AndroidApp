package org.hinanawiyuzu.qixia.preferences

import android.content.*
import androidx.datastore.core.*
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*

private const val PREFERENCES_NAME = "alarm_preferences"

// 注2：2021.2.24 自alpha07开始废弃了Context.createDataStore的API,我被AI坑了
// 还有这个Preferences的导入也要注意，不要导错了
val Context.alarmDataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class AlarmPreferences(
    private val context: Context,
    name: String
) {
    private val dataStore: DataStore<Preferences> get() = context.alarmDataStore
    private val counter = intPreferencesKey(name)

    suspend fun read(): Int? {
        return dataStore.data.first()[counter]
    }

    suspend fun decrement() {
        dataStore.edit {
            val current = it[counter] ?: return@edit
            if (current > 0)
                it[counter] = current - 1
        }
    }

    suspend fun set(counts: Int) {
        dataStore.edit {
            it[counter] = counts
        }
    }
}