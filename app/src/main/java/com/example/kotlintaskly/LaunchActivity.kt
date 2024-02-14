package com.example.kotlintaskly

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LaunchActivity : AppCompatActivity() {


    private val dataModel by viewModels<LaunchVModel>()
    private var pin: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !dataModel.isReady.value
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
        setContentView(R.layout.activity_main)

        if(pin == null) {
            replaceFragment(TutorialFragment())
        }

        else if(pin == "none") {
            switchActivity()
        }

        else {
            replaceFragment(PasscodeFragment())
        }

        // enables activity to know when fragment skip bttn is hit
        dataModel.skip.observe(this, Observer {
            if(dataModel.skip.value.toString() == "Skip Key Hit!") {
                Toast.makeText(this, "Detected skip bttn...", Toast.LENGTH_SHORT).show()
                switchActivity()
            }
        })

        dataModel.pin.observe(this) {
            pin = dataModel.returnPin()
            Toast.makeText(this, "Pin val in View Model...$pin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun switchActivity() {
        val intent = Intent(this, TaskActivity::class.java)
        startActivity(intent)
    }

    private fun replaceFragment(fragment : Fragment) {
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.add(R.id.fragmentContainerView, fragment)
        fragTransaction.commit()
    }
}