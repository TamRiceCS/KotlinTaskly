package com.example.kotlintaskly

import TaskAdapter
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.DragEvent.ACTION_DRAG_EXITED
import android.view.DragEvent.ACTION_DRAG_STARTED
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TaskActivity : AppCompatActivity(), View.OnClickListener {

    val data1 = ArrayList<TaskData>()
    val data2 = ArrayList<TaskData>()
    val data3 = ArrayList<TaskData>()

    val adapter1 = TaskAdapter(data1, "section1")
    val adapter2 = TaskAdapter(data2, "section2")
    val adapter3 = TaskAdapter(data3, "section3")

    private lateinit var menuBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val rv1 = findViewById<RecyclerView>(R.id.section1)
        val rv2 = findViewById<RecyclerView>(R.id.section2)
        val rv3 = findViewById<RecyclerView>(R.id.section3)

        rv1.layoutManager = LinearLayoutManager(this)
        rv2.layoutManager = LinearLayoutManager(this)
        rv3.layoutManager = LinearLayoutManager(this)

        rv1.adapter = adapter1
        rv2.adapter = adapter2
        rv3.adapter = adapter3

        var addTask : FloatingActionButton = findViewById(R.id.fab)
        addTask.setOnClickListener(this)

        menuBar = findViewById(R.id.bottomNavigationView)
        menuBar.menu.getItem(0).setChecked(true)

        menuBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    Toast.makeText(this@TaskActivity, "Already Here", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.calendar -> {
                    Toast.makeText(this@TaskActivity, "Calendar", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.stats -> {
                    Toast.makeText(this@TaskActivity, "Statistics", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, StatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings-> {
                    Toast.makeText(this@TaskActivity, "Settings", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    false
                }
            }
        }



    }

    override fun onClick(p0: View?) {
        if(p0!!.id == com.example.kotlintaskly.R.id.fab) {
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popUpView = inflater.inflate(R.layout.popup_addtask, null)

            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val popupWindow = PopupWindow(popUpView, width, height, true)

            popupWindow.showAtLocation(p0, Gravity.CENTER, 0, 0)

            val addText : EditText = (popUpView.findViewById(R.id.userTaskInput))
            val addTaskPop : Button = (popUpView.findViewById(R.id.addTask))
            val cancelTaskPop : Button = (popUpView.findViewById(R.id.cancelTask))

            // Make and populate spinner with values
            val spinner: Spinner = (popUpView.findViewById(R.id.spinner))
            val languages = resources.getStringArray(R.array.Sections)
            var section:String = "section1"

            ArrayAdapter.createFromResource(
                this,
                R.array.Sections,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears.
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner.
                spinner.adapter = adapter
            }

            addTaskPop.setOnClickListener(View.OnClickListener {
                var insertData = TaskData("Add Button Clicked", section, "today")
                val task = addText.text.toString()

                if(task == "") {
                    Toast.makeText(this@TaskActivity, "Task Does Not Contain Any Text", Toast.LENGTH_SHORT).show()
                }
                else if(section == "Morning") {
                    insertData.task = addText.text.toString()
                    if(data1.size == 0) {
                        adapter1.addAndInform(insertData, 0)
                    }
                    else {
                        adapter1.addAndInform(insertData, data1.size)
                    }
                }
                else if(section == "Afternoon") {
                    insertData.task = addText.text.toString()
                    if(data2.size == 0) {
                        adapter2.addAndInform(insertData, 0)
                    }
                    else {
                        adapter2.addAndInform(insertData, data2.size)
                    }
                }
                else if(section == "Night") {
                    insertData.task = addText.text.toString()
                    if(data3.size == 0) {
                        adapter3.addAndInform(insertData, 0)
                    }
                    else {
                        adapter3.addAndInform(insertData, data3.size)
                    }
                }
                popupWindow.dismiss()
            })

            cancelTaskPop.setOnClickListener(View.OnClickListener {
                popupWindow.dismiss()
            })

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    section = languages[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    section = "position1"
                }
            }


        }
    }


}