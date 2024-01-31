package com.example.kotlintaskly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class TutorialFragment : Fragment(), View.OnClickListener {

    private var passStatus : String? = null
    private var bundle : Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            passStatus = savedInstanceState.getString("Passcode")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_tutorial, container, false)
        var skipButton: Button = view.findViewById(R.id.skipButton)
        skipButton.setOnClickListener(this);

        if(passStatus  == null) {

        }
        Toast.makeText(activity, "Our current passcode value is... $passStatus", Toast.LENGTH_LONG ).show()
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
            replaceFragment(PasscodeFragment())
            Toast.makeText(activity, "Transferring Fragments...", Toast.LENGTH_SHORT).show()
        }
    }
}