package com.example.kotlintaskly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalendarActivity : AppCompatActivity() {
    private lateinit var menuBar: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        menuBar = findViewById(R.id.bottomNavigationView)
        menuBar.menu.getItem(1).setChecked(true)

        menuBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    menuBar.menu.getItem(1).setChecked(true)
                    val intent = Intent(this, TaskActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.calendar -> {
                    Toast.makeText(this@CalendarActivity, "Already Here", Toast.LENGTH_SHORT).show()

                    true
                }
                R.id.stats -> {
                    menuBar.menu.getItem(1).setChecked(true)
                    val intent = Intent(this, StatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings-> {
                    menuBar.menu.getItem(1).setChecked(true)
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

}