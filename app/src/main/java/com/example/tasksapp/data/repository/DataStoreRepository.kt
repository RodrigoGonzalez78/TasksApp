package com.example.tasksapp.data.repository

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val dataStore: androidx.datastore.core.DataStore<Preferences>
) {
    private val JWT_KEY = stringPreferencesKey("jwt_token")

    suspend fun saveJwt(jwt: String) {
        dataStore.edit { preferences ->
            preferences[JWT_KEY] = jwt
        }
    }

    fun getJwt(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[JWT_KEY]
        }
    }

    suspend fun deleteJwt() {
        dataStore.edit { preferences ->
            preferences.remove(JWT_KEY)
        }
    }
}