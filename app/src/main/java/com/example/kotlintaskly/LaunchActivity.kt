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

    // Data Model is used to track data as it changes live!
    private val dataModel by viewModels<LaunchVModel>()
    private var pin: String? = null
    private var start = true
    override fun onCreate(savedInstanceState: Bundle?) {

        // Plays launch animation (The plane that takes off)
        installSplashScreen().apply {
            // We need to make sure we have received the pin, continuing w/o causes unexpected behavior
            setKeepOnScreenCondition {
                !dataModel.isReady.value
            }

            // Make the icon using Figma
            // Use https://shapeshifter.design/ and take the code of the animation made, check avd_tasklyplane for how
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

        // Once the pin is set, let's place the correct fragment to run!
        dataModel.pin.observe(this) {
            pin = dataModel.returnPin()
            if (start) {
                when (pin) {
                    "tutorial" -> {
                        replaceFragment(TutorialFragment(), "tutorial")
                    }
                    "none" -> {
                        switchActivity()
                    }
                    else -> {
                        replaceFragment(PasscodeFragment(), "passcode")
                    }
                }

                start = false
            }
        }

        // Observe to see if skip button is hit, if so switch activity
        // If a passcode is entered and correct, switch activity
        dataModel.skip.observe(this, Observer {
            if (dataModel.skip.value.toString() == "Skip Key Hit!") {
                switchActivity()
            }
            if (dataModel.skip.value.toString() == "Correct Passcode") {
                switchActivity()
            }
        })

        // Once a user inputs an email that is saved to the datamodel, switch activity
        dataModel.email.observe(this) {
            switchActivity()
        }

    }

    private fun switchActivity() {
        val intent = Intent(this, TaskActivity::class.java)
        startActivity(intent)
    }


    private fun replaceFragment(fragment : Fragment, identity: String) {
        val bundle = Bundle()
        bundle.putString("case", identity)
        fragment.arguments = bundle
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.add(R.id.fragmentContainerView, fragment)
        fragTransaction.addToBackStack(identity)
        fragTransaction.commit()
    }
}