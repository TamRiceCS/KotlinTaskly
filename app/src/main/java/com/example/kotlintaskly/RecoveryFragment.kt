package com.example.kotlintaskly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText

class RecoveryFragment : Fragment(), View.OnClickListener {

    private var email: String? = null
    private var emailBox1: TextInputEditText? = null
    private var emailBox2: TextInputEditText? = null

    private var submitBttn : Button? = null
    private var skipBttn: Button? = null

    private var mode : String? = null

    private val viewModel by activityViewModels<LaunchVModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_recovery, container, false)

        mode = arguments?.getString("case")

        emailBox1 =  view.findViewById(R.id.initEmail)
        emailBox2 = view.findViewById(R.id.confirmEmail)
        submitBttn = view.findViewById(R.id.submit)
        skipBttn = view.findViewById(R.id.skip)

        submitBttn!!.setOnClickListener(this)
        skipBttn!!.setOnClickListener(this)

        return view
    }

    override fun onClick(bttn: View) {
        if(bttn.id == R.id.submit) {
            if(emailBox1!!.text.toString() == emailBox2!!.text.toString() && emailBox1!!.text.toString() != "") {
                if(emailBox1!!.text.toString().endsWith("@gmail.com")) {
                   if(mode == "new") {
                       email = emailBox1!!.text.toString()
                       viewModel.email.value = email
                   }
                    else {
                        replaceFragment(SettingsOptionsFragment(), "done")
                   }
                }
                else {
                    Toast.makeText(activity, "Please use a gmail account...", Toast.LENGTH_SHORT).show()
                }
            }
            else if(emailBox1!!.text.toString() == "" || emailBox2!!.text.toString() == "") {
                Toast.makeText(activity, "Please enter and confirm your email in both boxes...", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity, "The boxes do not match...", Toast.LENGTH_SHORT).show()
            }
        }

        if(bttn.id == R.id.skip) {
            viewModel.pin.value = "none"
            viewModel.skip.value = "Skip Key Hit!"
        }
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

}