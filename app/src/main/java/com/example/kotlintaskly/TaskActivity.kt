package com.example.kotlintaskly

import TaskAdapter
import android.R
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.kotlintaskly.R.layout.activity_task)

        val rv1 = findViewById<RecyclerView>(com.example.kotlintaskly.R.id.section1)
        val rv2 = findViewById<RecyclerView>(com.example.kotlintaskly.R.id.section2)
        val rv3 = findViewById<RecyclerView>(com.example.kotlintaskly.R.id.section3)

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
            var insertData = TaskData("example", "section1", "today")
            adapter1.addAndInform(insertData, 0)
        }
    }

}