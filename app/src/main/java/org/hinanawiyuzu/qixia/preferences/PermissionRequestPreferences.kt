package org.hinanawiyuzu.qixia.preferences

import android.content.*
import android.util.*
import androidx.datastore.core.*
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*

private const val PREFERENCES_NAME = "permission_request_preferences"
val Context.permissionRequestDataStore by preferencesDataStore(name = PREFERENCES_NAME)

class PermissionRequestPreferences(
    private val context: Context,
    permissionName: String
) {
    private val dataStore: DataStore<Preferences> get() = context.permissionRequestDataStore
    private val key = booleanPreferencesKey(permissionName)
    suspend fun set(value: Boolean) {
        dataStore.edit {
            it[key] = value
            Log.e("qixia", "set: ${it[key]}")
        }
    }

    suspend fun read(): Boolean? {
        return dataStore.data.first()[key]
    }

    suspend fun init() {
        dataStore.edit {
            it[key] = false
        }
    }
}