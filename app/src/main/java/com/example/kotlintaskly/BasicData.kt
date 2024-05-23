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
    // Datastores act like a txt file to store basic data, this is NOT encrypted! It is in the form of key-val pairs
    // We will need to do 3 main things: place data in (set), read data (retrieve), and check if data exists (contains)
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