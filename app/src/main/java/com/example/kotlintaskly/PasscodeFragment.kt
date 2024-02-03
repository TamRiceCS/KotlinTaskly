package com.example.kotlintaskly

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PasscodeFragment : Fragment(), View.OnClickListener, View.OnTouchListener {
    private var passcode: String? = null
    private var pin1 : EditText? = null
    private var pin2 : EditText? = null
    private var pin3 : EditText? = null
    private var pin4 : EditText? = null
    private var step = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_passcode, container, false)
        var instruction : TextView = view.findViewById(R.id.instructionPin)
        var recover : Button = view.findViewById(R.id.recoverBttn)
        var retry : Button = view.findViewById(R.id.retryBttn)
        pin1 = view.findViewById(R.id.pin1)
        pin2 = view.findViewById(R.id.pin2)
        pin3 = view.findViewById(R.id.pin3)
        pin4 = view.findViewById(R.id.pin4)


        if(passcode == null) {
            instruction.setText("Set a new Passcode")
            recover.setText("Skip Passcode")
            retry.visibility = View.INVISIBLE

        }

        // set pins focus on the start
        pin1!!.setOnTouchListener(this)
        pin2!!.setOnTouchListener(this)
        pin3!!.setOnTouchListener(this)
        pin4!!.setOnTouchListener(this)

        return view
    }

    private fun replaceFragment(fragment : Fragment) {
        val fragManager = parentFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragmentContainerView, fragment)
        fragTransaction.commit()
    }

    override fun onClick(bttn: View) {
        if(step == 1 && bttn.id == R.id.skipButton) {
            passcode = "none"
        }
    }

    override fun onTouch(clicked: View?, event: MotionEvent?): Boolean {

        // TODO: this could theoretically be made a switch statement
        if(clicked!!.id == R.id.pin1 && event!!.action == MotionEvent.ACTION_UP) {
            pin1!!.text.clear()
        }
        else if(clicked!!.id == R.id.pin2 && event!!.action == MotionEvent.ACTION_UP) {
            pin2!!.text.clear()
        }
        else if(clicked!!.id == R.id.pin3 && event!!.action == MotionEvent.ACTION_UP) {
            pin3!!.text.clear()
        }
        else if(clicked!!.id == R.id.pin4 && event!!.action == MotionEvent.ACTION_UP) {
            pin4!!.text.clear()
        }

        return false
    }

}