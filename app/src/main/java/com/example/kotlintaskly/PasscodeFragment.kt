package com.example.kotlintaskly

import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.runBlocking


class PasscodeFragment : Fragment(), View.OnClickListener, View.OnTouchListener {
    private var passcode: String? = null
    private var instructionText: TextView? = null
    private var recoverBttn: Button? = null
    private var submit : Button? = null
    private var retry: Button? = null
    private var pin1 : EditText? = null
    private var pin2 : EditText? = null
    private var pin3 : EditText? = null
    private var pin4 : EditText? = null
    private var card1: CardView? = null
    private var card2: CardView? = null
    private var card3: CardView? = null
    private var card4: CardView? = null
    private var mode : String? = null
    private var step = 1

    private val viewModel by activityViewModels<LaunchVModel>()
    private val taskModel by activityViewModels<TaskVModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_passcode, container, false)

        mode = arguments?.getString("case")


        instructionText = view.findViewById(R.id.instructionPin)
        recoverBttn  = view.findViewById(R.id.recoverSkipBttn)
        retry = view.findViewById(R.id.retryBttn)
        submit = view.findViewById(R.id.submitBttn)
        instructionText = view.findViewById(R.id.instructionPin)

        pin1 = view.findViewById(R.id.pin1)
        pin2 = view.findViewById(R.id.pin2)
        pin3 = view.findViewById(R.id.pin3)
        pin4 = view.findViewById(R.id.pin4)

        card1 = view.findViewById(R.id.pin1Card)
        card2 = view.findViewById(R.id.pin2Card)
        card3 = view.findViewById(R.id.pin3Card)
        card4 = view.findViewById(R.id.pin4Card)



        if(mode == "new") {
            instructionText!!.setText("New Passcode")
            recoverBttn!!.setText("Skip Passcode")
            retry?.visibility = View.INVISIBLE

        }

        else if(mode == "reset"){
            instructionText!!.setText("Reset Passcode")
            recoverBttn!!.setText("Cancel Reset")
            retry?.visibility = View.INVISIBLE
        }

        // return
        else{
            instructionText!!.setText("Enter Passcode")
            recoverBttn!!.setText("Recover Passcode")
            retry?.visibility = View.INVISIBLE
            step = 3
        }

        pin1!!.setOnTouchListener(this)
        pin2!!.setOnTouchListener(this)
        pin3!!.setOnTouchListener(this)
        pin4!!.setOnTouchListener(this)

        // set button onClickListeners
        submit!!.setOnClickListener(this)
        recoverBttn!!.setOnClickListener(this)
        retry!!.setOnClickListener(this)

        return view
    }

    private fun replaceFragment(fragment : Fragment, identity : String) {
        val bundle = Bundle()
        bundle.putString("case", identity)
        fragment.arguments = bundle
        val fragManager = parentFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragmentContainerView, fragment)
        fragTransaction.commit()
    }

    override fun onClick(bttn: View) {

        when(bttn.id) {
            R.id.recoverSkipBttn -> {
                // cancel reset
                if(mode == "reset") {
                    replaceFragment(SettingsOptionsFragment(), "none")
                }
                // skip this whole pin setting process
                else if(step == 1 || step == 2) {
                    runBlocking {
                        viewModel.data("none")
                        viewModel.skip.value = "Skip Key Hit!"
                    }
                }
                // passcode recovery button
                else if(mode == "return") {
                    Toast.makeText(activity, "Please check your email for recovery steps...", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.submitBttn -> {
                // step 1: choose a pin to set
                if(step == 1) {
                    var passcodeAttempt: String = pin1!!.text.toString() + pin2!!.text.toString() + pin3!!.text.toString() + pin4!!.text.toString()
                    if(passcodeAttempt.length == 4) {
                        instructionText!!.text = "Please confirm pin"

                        pin1!!.text.clear()
                        pin2!!.text.clear()
                        pin3!!.text.clear()
                        pin4!!.text.clear()

                        card1!!.setBackgroundColor(getResources().getColor(R.color.lightForestGreen))
                        card2!!.setBackgroundColor(getResources().getColor(R.color.lightForestGreen))
                        card3!!.setBackgroundColor(getResources().getColor(R.color.lightForestGreen))
                        card4!!.setBackgroundColor(getResources().getColor(R.color.lightForestGreen))
                        instructionText!!.setBackgroundColor(getResources().getColor(R.color.lightForestGreen))
                        step = 2;
                        passcode = passcodeAttempt
                        retry?.visibility = View.VISIBLE
                    }
                    else {
                        Toast.makeText(activity, "All pin boxes must be filled...", Toast.LENGTH_SHORT).show()
                    }
                }

                // step 2: confirm pin to set
                if(step == 2) {
                    var passcodeAttempt: String = pin1!!.text.toString() + pin2!!.text.toString() + pin3!!.text.toString() + pin4!!.text.toString()

                    if(passcodeAttempt == passcode) {
                        runBlocking {
                            if(mode == "new") {
                                replaceFragment(RecoveryFragment(), "new")
                                viewModel.data(passcodeAttempt)
                            }
                            else {
                                Log.d("Run Order", "Ran reset mode code...")
                                taskModel.setPin(passcodeAttempt)
                                replaceFragment(SettingsOptionsFragment(), passcodeAttempt)
                            }
                        }
                    }

                    else if(passcodeAttempt != ""){
                        Toast.makeText(activity, "Oops, passcode doesn't match...", Toast.LENGTH_SHORT).show()
                    }
                }

                // mode 3: user returns and enters pin
                if(step == 3) {
                    var backendPass = viewModel.returnPin()
                    var passcodeAttempt: String = pin1!!.text.toString() + pin2!!.text.toString() + pin3!!.text.toString() + pin4!!.text.toString()

                    if(backendPass == passcodeAttempt) {
                        viewModel.skip.value = "Correct Passcode"
                    }
                    else {
                        Toast.makeText(activity, "Oops, passcode doesn't match...", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.retryBttn -> {
                instructionText!!.text = "Please Enter Your Pin"

                pin1!!.text.clear()
                pin2!!.text.clear()
                pin3!!.text.clear()
                pin4!!.text.clear()

                card1!!.setBackgroundColor(getResources().getColor(R.color.forestGreen))
                card2!!.setBackgroundColor(getResources().getColor(R.color.forestGreen))
                card3!!.setBackgroundColor(getResources().getColor(R.color.forestGreen))
                card4!!.setBackgroundColor(getResources().getColor(R.color.forestGreen))
                instructionText!!.setBackgroundColor(getResources().getColor(R.color.forestGreen))

                step = 1
                passcode = null
                retry?.visibility = View.INVISIBLE
            }
        }
    }

    override fun onTouch(clicked: View?, event: MotionEvent?): Boolean {

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