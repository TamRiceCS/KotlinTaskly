package com.example.kotlintaskly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import java.time.LocalDate

class EditDiaryFragment : Fragment(), View.OnClickListener {

    private val viewModel by activityViewModels<DiaryVModel>()
    private lateinit var discard : Button
    private lateinit var publish: Button
    private lateinit var title: EditText
    private lateinit var entry : EditText
    private var mode: String? = null
    private var date : String? = null



    // if you receive newEdit, it is a new entry
    // if your receive oldEdit, we are changing an old entry

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_diary, container, false)
        mode = arguments?.getString("case")


        title = view.findViewById(R.id.title)
        entry = view.findViewById(R.id.entryText)
        discard = view.findViewById(R.id.discard)
        publish = view.findViewById(R.id.publish)

        discard.setOnClickListener(this)
        publish.setOnClickListener(this)


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

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.discard -> {
                replaceFragment(DiaryFragment(), "diary")
            }
            R.id.publish -> {
                if(mode == "new")
                    if(entry.text.toString() == "") {
                        Toast.makeText(activity, "Can't publish an empty entry", Toast.LENGTH_SHORT).show()
                    }
                else{
                        Toast.makeText(activity, "Published your entry", Toast.LENGTH_SHORT).show()
                        date = LocalDate.now().toString()
                        var insertEntry = DiaryEntity(title.text.toString(), entry.text.toString(), date!!, "Published")
                        if(title.text.toString() == "") {
                            insertEntry.title = date.toString()
                        }
                        viewModel.insertBacklog(insertEntry);
                        replaceFragment(DiaryFragment(), "done")
                    }
            }
        }
    }

}