package com.example.kotlintaskly

import android.content.Context
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DragImplementation(context: Context) : View.OnDragListener {
    override fun onDrag(p0: View?, p1: DragEvent?): Boolean {
        return true
    }
}