package com.example.kotlintaskly

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer


class LaunchActivity : AppCompatActivity() {


    private val dataModel by viewModels<LaunchVModel>()
    private var pin: String? = null
    private var start = true
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !dataModel.isReady.value
            }

            setOnExitAnimationListener { screen ->
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
        setContentView(R.layout.activity_main)

        // handles pin instance
        dataModel.pin.observe(this) {
            pin = dataModel.returnPin()
            if (start) {
                if (pin == "tutorial") {
                    replaceFragment(TutorialFragment(), "tutorial")
                    Log.d("Run Order", "Loading 1st Frame")
                } else if (pin == "none") {
                    switchActivity()
                } else {
                    replaceFragment(PasscodeFragment(), "passcode")
                }

                start = false
            }
        }

        // enables activity to know when fragment skip bttn is hit
        dataModel.skip.observe(this, Observer {
            if (dataModel.skip.value.toString() == "Skip Key Hit!") {
                switchActivity()
            }
            if (dataModel.skip.value.toString() == "Correct Passcode") {
                switchActivity()
            }
        })

        dataModel.email.observe(this, {
            switchActivity()
        })

    }

    private fun switchActivity() {
        val intent = Intent(this, TaskActivity::class.java)
        startActivity(intent)
    }

    private fun replaceFragment(fragment : Fragment, identity: String) {
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.add(R.id.fragmentContainerView, fragment)
        fragTransaction.addToBackStack(identity)
        fragTransaction.commit()
    }
}