package org.hinanawiyuzu.qixia.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

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