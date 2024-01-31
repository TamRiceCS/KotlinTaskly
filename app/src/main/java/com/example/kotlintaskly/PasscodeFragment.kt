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
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PasscodeFragment : Fragment(), View.OnClickListener, View.OnTouchListener {
    private lateinit var dataStore: DataStore<Preferences>
    private var passcode: String? = null
    private var pin1 : EditText? = null



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
        var pin2 : EditText = view.findViewById(R.id.pin2)
        var pin3 : EditText = view.findViewById(R.id.pin3)
        var pin4 : EditText = view.findViewById(R.id.pin4)


        if(passcode == null) {
            instruction.setText("Set a new Passcode")
            recover.setText("Skip Passcode")
            retry.visibility = View.INVISIBLE

        }

        // set pins focus on the start
        pin1!!.setOnClickListener(this)
        //pin1!!.focusable = View.FOCUSABLE
        //pin1!!.requestFocus()

        return view
    }

    private fun replaceFragment(fragment : Fragment) {
        val fragManager = parentFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragmentContainerView, fragment)
        fragTransaction.commit()
    }

    override fun onClick(bttn: View) {
        if(bttn.getId() == R.id.skipButton) {
            passcode = "none"
        }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }
}