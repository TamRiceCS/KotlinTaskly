package com.example.kotlintaskly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DiaryFragment : Fragment(), View.OnClickListener {

    private lateinit var drafts : Button
    private lateinit var add: Button
    val data = ArrayList<DiaryEntity>()
    val adapter = DiaryAdapter(data)
    private val diaryModel by activityViewModels<DiaryVModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_diary, container, false)

        drafts = view.findViewById(R.id.drafts)
        add = view.findViewById(R.id.add)

        val rv = view.findViewById<RecyclerView>(R.id.diaryRV)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(activity)

        add.setOnClickListener(this)

        diaryModel.fetch("Published")

        diaryModel.publishedEntries.observe(viewLifecycleOwner) {
            adapter.populate(ArrayList(diaryModel.backendPublished))
        }



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
            R.id.add -> {
                replaceFragment(EditDiaryFragment(), "new")
            }
        }
    }

}