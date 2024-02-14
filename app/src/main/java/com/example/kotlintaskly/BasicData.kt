package com.example.kotlintaskly

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class BasicData(context: Context) {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "LaunchSettings")
    private val dataStore = context.dataStore



    suspend fun set(key : String, given : String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit{ basics ->
            basics[dataStoreKey] = given
        }
    }

    suspend fun retrieve(key: String) : String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

}