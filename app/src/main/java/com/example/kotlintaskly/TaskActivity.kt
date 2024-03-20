package com.example.kotlintaskly

import TaskAdapter
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TaskActivity : AppCompatActivity(), View.OnClickListener {

    val data1 = ArrayList<TaskData>()
    val data2 = ArrayList<TaskData>()
    val data3 = ArrayList<TaskData>()

    val adapter1 = TaskAdapter(data1, "section1")
    val adapter2 = TaskAdapter(data2, "section2")
    val adapter3 = TaskAdapter(data3, "section3")

    private lateinit var popUp: PopupWindow

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

        var addTask : FloatingActionButton = findViewById(com.example.kotlintaskly.R.id.fab)
        addTask.setOnClickListener(this)


    }

    override fun onClick(p0: View?) {
        if(p0!!.id == com.example.kotlintaskly.R.id.fab) {

            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popUpView = inflater.inflate(R.layout.popup_addtask, null)

            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val popupWindow = PopupWindow(popUpView, width, height, true)

            popupWindow.showAtLocation(p0, Gravity.CENTER, 0, 0)

            val addTaskPop : Button = (popUpView.findViewById(R.id.addTask))
            val cancelTaskPop : Button = (popUpView.findViewById(R.id.cancelTask))

            addTaskPop.setOnClickListener(View.OnClickListener {
                var insertData = TaskData("Add Button Clicked", "section1", "today")
                adapter1.addAndInform(insertData, 0)
                popupWindow.dismiss()
            })

            cancelTaskPop.setOnClickListener(View.OnClickListener {
                var insertData = TaskData("Cancel Button Clicked", "section1", "today")
                adapter1.addAndInform(insertData, 0)
                popupWindow.dismiss()
            })

        }
    }



}