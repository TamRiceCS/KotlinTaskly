package com.example.kotlintaskly

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BasicData (context: Context) {
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

    fun contains(key: String) : Boolean {
        val dataStoreKey = stringPreferencesKey(key)
        var fact : Boolean = false
        dataStore.data.map { preference ->
            fact = preference.contains(dataStoreKey)
        }
        return fact
    }
}