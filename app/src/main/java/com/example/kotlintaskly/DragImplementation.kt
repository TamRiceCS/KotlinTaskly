package com.example.kotlintaskly

import android.content.Context
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DragImplementation(context: Context) : View.OnDragListener {
    override fun onDrag(p0: View?, p1: DragEvent?): Boolean {
        when(p1!!.action) {
            DragEvent.ACTION_DROP -> {
                val layout = p1.localState as? View
                val source = layout!!.parent
                val recyclerView = source!!.parent as RecyclerView
                val orginAdapter = recyclerView.adapter

                if(p0!!.id == R.id.section1) {
                    Log.d("Drag Event", "destination is section1")
                    if(recyclerView.id == R.id.section1) {
                        Log.d("Drag Event", "orgin is section1")
                    }
                    if(recyclerView.id == R.id.section2) {
                        Log.d("Drag Event", "orgin is section2")
                    }
                    if(recyclerView.id == R.id.section3) {
                        Log.d("Drag Event", "orgin is section3")
                    }
                }

                if(p0!!.id == R.id.section2) {
                    Log.d("Drag Event", "destination is section2")
                    if(recyclerView.id == R.id.section1) {
                        Log.d("Drag Event", "orgin is section1")
                    }
                    if(recyclerView.id == R.id.section2) {
                        Log.d("Drag Event", "orgin is section2")
                    }
                    if(recyclerView.id == R.id.section3) {
                        Log.d("Drag Event", "orgin is section3")
                    }
                }

                if(p0!!.id == R.id.section3) {
                    Log.d("Drag Event", "destination is section3")
                    if(recyclerView.id == R.id.section1) {
                        Log.d("Drag Event", "orgin is section1")
                    }
                    if(recyclerView.id == R.id.section2) {
                        Log.d("Drag Event", "orgin is section2")
                    }
                    if(recyclerView.id == R.id.section3) {
                        Log.d("Drag Event", "orgin is section3")
                     }
                }
                return false // return true if successfully placed, else return false
            }
        }
        return true
    }
}