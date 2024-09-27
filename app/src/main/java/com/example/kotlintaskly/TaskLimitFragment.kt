package com.example.kotlintaskly

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.activityViewModels

class TaskLimitFragment : Fragment(), View.OnClickListener {

    private var spinner1: Spinner? = null
    private var spinner2: Spinner? = null
    private var spinner3: Spinner? = null
    private var setLimit: Button? = null
    private var cancel: Button? = null

    private val taskModel by activityViewModels<TaskVModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_task_limit, container, false)
        // Inflate the layout for this fragment

        spinner1 = view!!.findViewById(R.id.spinnerSection1)
        spinner2 = view!!.findViewById(R.id.spinnerSection2)
        spinner3 = view!!.findViewById(R.id.spinnerSection3)

        setLimit = view!!.findViewById(R.id.setLimit)
        cancel = view!!.findViewById(R.id.cancel)


        // Make and populate spinner with values

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.TimeLimits,
            R.layout.spinner_style
        ).also { adapter1 ->
            // Specify the layout to use when the list of choices appears.
            adapter1.setDropDownViewResource(R.layout.spinner_style)
            // Apply the adapter to the spinner.
            spinner1!!.adapter = adapter1
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.TimeLimits,
            R.layout.spinner_style
        ).also { adapter2 ->
            // Specify the layout to use when the list of choices appears.
            adapter2.setDropDownViewResource(R.layout.spinner_style)
            // Apply the adapter to the spinner.
            spinner2!!.adapter = adapter2
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.TimeLimits,
            R.layout.spinner_style
        ).also { adapter3 ->
            // Specify the layout to use when the list of choices appears.
            adapter3.setDropDownViewResource(R.layout.spinner_style)
            // Apply the adapter to the spinner.
            spinner3!!.adapter = adapter3
        }


        cancel!!.setOnClickListener(this)
        setLimit!!.setOnClickListener(this)

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
        if(bttn.id == R.id.cancel) {
            replaceFragment(SettingsOptionsFragment(), "done")
        }
        else{
            taskModel.setLimit(spinner1!!.selectedItemPosition + 1, spinner2!!.selectedItemPosition + 1, spinner3!!.selectedItemPosition + 1)
            Log.d("Checked", "spinner 1: " + (spinner1!!.selectedItemPosition + 1).toString())
            Log.d("Checked", "spinner 2: " + (spinner2!!.selectedItemPosition + 1).toString())
            Log.d("Checked", "spinner 3: " + (spinner3!!.selectedItemPosition + 1).toString())
            replaceFragment(SettingsOptionsFragment(), "done")
        }
    }


}