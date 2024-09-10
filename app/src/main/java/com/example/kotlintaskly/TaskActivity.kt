package com.example.kotlintaskly

import TaskAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    val data1 = ArrayList<TaskEntity>()
    val data2 = ArrayList<TaskEntity>()
    val data3 = ArrayList<TaskEntity>()

    val adapter1 = TaskAdapter(data1, "First Section")
    val adapter2 = TaskAdapter(data2, "Second Section")
    val adapter3 = TaskAdapter(data3, "Third Section")

    private val taskModel by viewModels<TaskVModel>()

    private lateinit var menuBar: BottomNavigationView
    private lateinit var dayBox: TextView
    private lateinit var topNav: LinearLayout
    private lateinit var timeWarning: TextView

    private lateinit var firstLimit: TextView
    private lateinit var secondLimit: TextView
    private lateinit var thirdLimit: TextView

    var dayNum: Long = 0
    val currentDate = LocalDate.now()
    val date = LocalDate.now()
    var dow: String = date.dayOfWeek.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        db = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "taskBacklog"
        ).build()

        dayBox = findViewById(R.id.dowText)
        dayBox.text = dow

        topNav = findViewById(R.id.topNavBG)
        timeWarning = findViewById(R.id.dowTime)

        var prior : ImageButton = findViewById(R.id.priorDay)
        var next : ImageButton = findViewById(R.id.nextDay)

        prior.setOnClickListener(this)
        next.setOnClickListener(this)

        firstLimit = findViewById(R.id.section1Limit)
        secondLimit = findViewById(R.id.section2Limit)
        thirdLimit = findViewById(R.id.section3Limit)

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
                R.id.diary -> {
                    Toast.makeText(this@TaskActivity, "Diary", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DiaryActivity::class.java)
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

        taskModel.fetch("First", date.toString())
        taskModel.fetch("Second", date.toString())
        taskModel.fetch("Third", date.toString())



        taskModel.firstTasks.observe(this) {
            adapter1.dayChange(ArrayList(taskModel.backendFirst))
            firstLimit.text = "Limit: " + taskModel.backendFirst.size.toString() + "/"  + taskModel.firstLimit
            Log.d("Run Order", "Observed a change in section 1 " + data1.size)
        }
        taskModel.secondTasks.observe(this) {
            adapter2.dayChange(ArrayList(taskModel.backendSecond))
            secondLimit.text = "Limit: " + taskModel.backendSecond.size.toString() + "/"  + taskModel.secondLimit
            Log.d("Run Order", "Observed a change in section 2 " + data2.size)
        }
        taskModel.thirdTasks.observe(this) {
            adapter3.dayChange(ArrayList(taskModel.backendThird))
            thirdLimit.text = "Limit: " + taskModel.backendThird.size.toString() + "/"  + taskModel.thirdLimit
            Log.d("Run Order", "Observed a change in section 3 " + data3.size)
        }
    }

    override fun onResume() {
        super.onResume()
        menuBar.menu.getItem(0).setChecked(true)
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
                var insertData = TaskEntity(0, "Add Button Clicked", section, date.toString())
                val task = addText.text.toString()

                if(task == "") {
                    Toast.makeText(this@TaskActivity, "Task Does Not Contain Any Text", Toast.LENGTH_SHORT).show()
                }
                else if(section == "First Section") {
                    insertData.task = addText.text.toString()
                    insertData.location = "First"
                    if(data1.size == 0) {
                        adapter1.addAndInform(insertData, 0)
                    }
                    else {
                        adapter1.addAndInform(insertData, data1.size)
                    }
                    taskModel.updateBacklog(insertData)
                    firstLimit.text = "Limit: " + data1.size.toString() + "/"  + taskModel.firstLimit

                }
                else if(section == "Second Section") {
                    insertData.task = addText.text.toString()
                    insertData.location = "Second"
                    if(data2.size == 0) {
                        adapter2.addAndInform(insertData, 0)
                    }
                    else {
                        adapter2.addAndInform(insertData, data2.size)
                    }
                    taskModel.updateBacklog(insertData)
                    secondLimit.text = "Limit: " + data2.size.toString() + "/"  + taskModel.secondLimit
                }
                else if(section == "Third Section") {
                    insertData.task = addText.text.toString()
                    insertData.location = "Third"
                    if(data3.size == 0) {
                        adapter3.addAndInform(insertData, 0)
                    }
                    else {
                        adapter3.addAndInform(insertData, data3.size)
                    }
                    taskModel.updateBacklog(insertData)
                    thirdLimit.text = "Limit: " + data3.size.toString() + "/"  + taskModel.thirdLimit
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
        if(p0!!.id == R.id.priorDay) {
            if(dayNum > -7) {
                dayNum--
                dayBox.text = date.dayOfWeek.plus(dayNum).toString()
                Log.d("Run Order", date.minusDays(dayNum).toString() + " " + dayNum)
                taskModel.fetch("First", date.minusDays(dayNum * -1).toString())
                taskModel.fetch("Second", date.minusDays(dayNum * -1).toString())
                taskModel.fetch("Third", date.minusDays(dayNum * -1).toString())
                changeTopNavDetails()
            }
        }

        if(p0!!.id == R.id.nextDay) {
            if(dayNum < 7) {
                dayNum++
                dayBox.text = date.dayOfWeek.plus(dayNum).toString()
                Log.d("Run Order", date.plusDays(dayNum).toString() + " " + dayNum)
                taskModel.fetch("First", date.plusDays(dayNum).toString())
                taskModel.fetch("Second", date.plusDays(dayNum).toString())
                taskModel.fetch("Third", date.plusDays(dayNum).toString())
                changeTopNavDetails()
            }
        }
    }

    fun changeTopNavDetails() {
        if(dayNum > 0) {
            topNav.setBackgroundResource(R.drawable.roundtransparent_lightforestgreenglass)
            timeWarning.text = "Future"
        }
        else if(dayNum < 0) {
            topNav.setBackgroundResource(R.drawable.roundtransparent_lightforestgreenglass)
            timeWarning.text = "Past"
        }
        else{
            topNav.setBackgroundResource(R.drawable.roundtransparent_forestgreenglass)
            timeWarning.text = "Present"
        }
    }

    companion object {
        lateinit var db: TaskDatabase
    }


}