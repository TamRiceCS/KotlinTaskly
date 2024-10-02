package com.example.kotlintaskly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DiaryFragment : Fragment(), View.OnClickListener {

    private lateinit var drafts : Button
    private lateinit var add: Button
    private lateinit var label : TextView
    val data = ArrayList<DiaryEntity>()
    private val adapter = DiaryAdapter(data)
    private val diaryModel by activityViewModels<DiaryVModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_diary, container, false)

        drafts = view.findViewById(R.id.drafts)
        add = view.findViewById(R.id.add)
        label = view.findViewById(R.id.entryType)

        val swipe = object : SwipeImplementation(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT-> {
                        diaryModel.deleteBacklog(data[viewHolder.layoutPosition].title, data[viewHolder.layoutPosition].date)
                    }
                    ItemTouchHelper.RIGHT -> {
                        diaryModel.deleteBacklog(data[viewHolder.layoutPosition].title, data[viewHolder.layoutPosition].date)
                    }
                }
            }
        }

        val rv = view.findViewById<RecyclerView>(R.id.diaryRV)
        val touchHelper = ItemTouchHelper(swipe)
        touchHelper.attachToRecyclerView(rv)
        rv.adapter = adapter

        adapter.setOnBttnClickListener(object: DiaryAdapter.onItemClickListener {
            override fun onClick(position: Int) {
                replaceFragment(EditDiaryFragment(),data[position].title, data[position].entry, data[position].date, "update")
            }
        })

        adapter.setOnCardClickListener(object: DiaryAdapter.onItemClickListener {
            override fun onClick(position: Int) {
                replaceFragment(EditDiaryFragment(),data[position].title, data[position].entry, data[position].date, "view")
            }
        })
        rv.layoutManager = LinearLayoutManager(activity)

        add.setOnClickListener(this)
        drafts.setOnClickListener(this)

        diaryModel.fetch("Published")

        diaryModel.publishedEntries.observe(viewLifecycleOwner) {
            adapter.populate(ArrayList(diaryModel.backendPublished))
        }

        diaryModel.draftedEntries.observe(viewLifecycleOwner) {
            adapter.populate(ArrayList(diaryModel.backendDrafted))
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

    private fun replaceFragment(fragment : Fragment, title : String, entry: String, date: String, case: String) {
        val bundle = Bundle()
        bundle.putString("case", case)
        bundle.putString("title", title)
        bundle.putString("text", entry)
        bundle.putString("date", date)
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
            R.id.drafts -> {
                if(label.text.toString() == "Reflection Entries") {
                    label.text = getString(R.string.reflection_drafts)
                    drafts.text = getString(R.string.published)
                    diaryModel.fetch("Drafted")
                }
                else{
                    label.text = getString(R.string.reflection_entries)
                    drafts.text = getString(R.string.drafts)
                    diaryModel.fetch("Published")
                }
            }
        }
    }

}