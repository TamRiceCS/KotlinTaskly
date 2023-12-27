package com.example.kotlintaskly

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<mVModel>()
    private var passcode: String? = null

    private lateinit var dataStore : DataStore<Preferences>
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }

            setOnExitAnimationListener {screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.TRANSLATION_X,
                    0f,
                    1000f
                )

                zoomX.interpolator = AccelerateDecelerateInterpolator()
                zoomX.duration = 300L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.TRANSLATION_Y,
                    0f,
                    -600f
                )
                zoomY.interpolator = AccelerateDecelerateInterpolator()
                zoomY.duration = 300L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }

        }

        super.onCreate(savedInstanceState)

        dataStore = createDataStore(name = "settings")

        lifecycleScope.launch {
            passcode = read("Passcode")
        }

        if(passcode == null) {
            //setContentView(R.layout.activity_main)
            setContentView(R.layout.activity_tutorial)
            val toast = Toast.makeText(this, "There IS NO passcode setting", Toast.LENGTH_LONG) // in Activity
            toast.show()
        }
        else {
            setContentView(R.layout.activity_tutorial)
            val toast = Toast.makeText(this, "There IS a passcode setting", Toast.LENGTH_LONG) // in Activity
            toast.show()
        }

    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String) : String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}