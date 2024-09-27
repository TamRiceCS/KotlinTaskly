package com.example.kotlintaskly

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels


class SettingsOptionsFragment : Fragment(), View.OnClickListener {
    private var resetPin : Button? = null
    private var resetEmail : Button? = null
    private var taskLimits : Button? = null
    private var sendFeedback : Button? = null
    private var clearData : Button? = null
    private var linkedin : ImageButton? = null
    private var github : ImageButton? = null

    private val taskModel by activityViewModels<TaskVModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_settings_options_fragments, container, false)

        val mode = arguments?.getString("case")

        if(mode != null && mode != "settings") {
            // taskModel.setPin(mode)
            // TODO: Figure out why this was commented out
        }

        resetPin = view.findViewById(R.id.resetPin)
        resetEmail = view.findViewById(R.id.resetEmail)
        taskLimits = view.findViewById(R.id.taskLimits)
        sendFeedback = view.findViewById(R.id.sendFeedback)
        clearData = view.findViewById(R.id.clearData)

        linkedin = view.findViewById(R.id.linkedIn)
        github = view.findViewById(R.id.github)

        resetPin!!.setOnClickListener(this)
        resetEmail!!.setOnClickListener(this)
        taskLimits!!.setOnClickListener(this)
        sendFeedback!!.setOnClickListener(this)
        clearData!!.setOnClickListener(this)
        linkedin!!.setOnClickListener(this)
        github!!.setOnClickListener(this)



        return view
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.resetPin -> {
                replaceFragment(PasscodeFragment(), "reset")
            }
            R.id.resetEmail -> {
                replaceFragment(RecoveryFragment(), "reset")
            }
            R.id.taskLimits -> {
                replaceFragment(TaskLimitFragment(), "reset")
            }
            R.id.sendFeedback -> {
                val openWebsite = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TamRiceCS/KotlinTaskly"))
                startActivity(openWebsite)
            }
            R.id.clearData -> {
                Toast.makeText(activity, "Not implemented yet", Toast.LENGTH_SHORT).show()
            }
            R.id.linkedIn -> {
                val openWebsite = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/tam-rice-0742431ba/"))
                startActivity(openWebsite)
            }
            R.id.github -> {
                val openWebsite = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TamRiceCS/KotlinTaskly"))
                startActivity(openWebsite)
            }
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