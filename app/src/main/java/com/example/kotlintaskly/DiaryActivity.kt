package com.example.kotlintaskly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView

class DiaryActivity : AppCompatActivity() {
    private lateinit var menuBar: BottomNavigationView
    private val diaryModel by viewModels<DiaryVModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        db = Room.databaseBuilder(
            applicationContext,
            DiaryDatabase::class.java,
            "diaryBacklog"
        ).build()

        menuBar = findViewById(R.id.bottomNavigationView)
        menuBar.menu.getItem(1).setChecked(true)

        menuBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    val intent = Intent(this, TaskActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.diary -> {
                    Toast.makeText(this@DiaryActivity, "Already Here", Toast.LENGTH_SHORT).show()

                    true
                }
                R.id.stats -> {
                    val intent = Intent(this, StatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings-> {
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

    override fun onResume() {
        super.onResume()
        menuBar.menu.getItem(1).setChecked(true)
        replaceFragment(DiaryFragment(), "diary")
    }

    private fun replaceFragment(fragment : Fragment, identity: String) {
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.add(R.id.fragmentContainerView, fragment)
        fragTransaction.addToBackStack(identity)
        fragTransaction.commit()
    }

    companion object {
        lateinit var db: DiaryDatabase
    }
}