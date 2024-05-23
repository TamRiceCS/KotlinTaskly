package com.example.kotlintaskly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {
    private lateinit var menuBar: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        replaceFragment(SettingsOptionsFragment(), "settings")

        menuBar = findViewById(R.id.bottomNavigationView)
        menuBar.menu.getItem(3).setChecked(true)

        menuBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    val intent = Intent(this, TaskActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.calendar -> {
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.stats -> {
                    val intent = Intent(this, StatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings-> {
                    Toast.makeText(this@SettingsActivity, "Already Here", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun replaceFragment(fragment : Fragment, identity: String) {
        val bundle = Bundle()
        bundle.putString("case", identity)
        fragment.arguments = bundle
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.add(R.id.fragmentContainerView, fragment)
        fragTransaction.addToBackStack(identity)
        fragTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        menuBar.menu.getItem(3).setChecked(true)
        replaceFragment(SettingsOptionsFragment(), "settings")
    }
}