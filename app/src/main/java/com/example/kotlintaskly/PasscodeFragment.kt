package com.example.kotlintaskly

import android.os.Bundle
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


class PasscodeFragment : Fragment(), View.OnClickListener, View.OnTouchListener {
    private var passcode: String? = null
    private var instructionText: TextView? = null
    private var submit : Button? = null
    private var pin1 : EditText? = null
    private var pin2 : EditText? = null
    private var pin3 : EditText? = null
    private var pin4 : EditText? = null
    private var card1: CardView? = null
    private var card2: CardView? = null
    private var card3: CardView? = null
    private var card4: CardView? = null
    private var step = 1

    private val viewModel by activityViewModels<LaunchVModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_passcode, container, false)
        var instruction : TextView = view.findViewById(R.id.instructionPin)
        var recoverSkip : Button = view.findViewById(R.id.recoverSkipBttn)
        var retry : Button = view.findViewById(R.id.retryBttn)
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



        if(passcode == null) {
            instruction.setText("Set a new Passcode")
            recoverSkip.setText("Skip Passcode")
            retry.visibility = View.INVISIBLE

        }

        // set pins focus on the start
        pin1!!.setOnTouchListener(this)
        pin2!!.setOnTouchListener(this)
        pin3!!.setOnTouchListener(this)
        pin4!!.setOnTouchListener(this)

        // set button onClickListeners
        submit!!.setOnClickListener(this)
        recoverSkip.setOnClickListener(this)

        return view
    }

    private fun replaceFragment(fragment : Fragment) {
        val fragManager = parentFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragmentContainerView, fragment)
        fragTransaction.commit()
    }

    override fun onClick(bttn: View) {
        if(step == 1 && bttn.id == R.id.recoverSkipBttn) {
            passcode = "none"
            viewModel.skip.value = "Skip Key Hit!"
        }
        if(step == 1 && bttn.id == R.id.submitBttn) {
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

                Toast.makeText(activity, "Confirm Your Pin Now...", Toast.LENGTH_SHORT).show()
                step = 2;
                passcode = passcodeAttempt
            }
            else {
                Toast.makeText(activity, "All pin boxes must be filled...", Toast.LENGTH_SHORT).show()
            }
        }

        if(step == 2 && bttn.id == R.id.submitBttn) {
            var passcodeAttempt: String = pin1!!.text.toString() + pin2!!.text.toString() + pin3!!.text.toString() + pin4!!.text.toString()

            if(passcodeAttempt == passcode) {
                viewModel.pin.value = passcode
                replaceFragment(RecoveryFragment())
            }

            else{
                Toast.makeText(activity, "Oops, passcode doesn't match...", Toast.LENGTH_SHORT).show()
            }
        }

        if(bttn.id == R.id.retryBttn) {
            instructionText!!.text = "Please Enter Your Pin"

            pin1!!.text.clear()
            pin2!!.text.clear()
            pin3!!.text.clear()
            pin4!!.text.clear()

            card1!!.setBackgroundColor(getResources().getColor(R.color.forestGreen))
            card2!!.setBackgroundColor(getResources().getColor(R.color.forestGreen))
            card3!!.setBackgroundColor(getResources().getColor(R.color.forestGreen))
            card4!!.setBackgroundColor(getResources().getColor(R.color.forestGreen))

            Toast.makeText(activity, "No problem...", Toast.LENGTH_SHORT).show()
            step = 1
            passcode = null

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