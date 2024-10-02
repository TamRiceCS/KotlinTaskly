package com.example.kotlintaskly

import android.content.ClipData
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private var mList: ArrayList<TaskEntity>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>(), View.OnTouchListener{
    private lateinit var completionListener: onItemClickListener

    interface onItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        completionListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates each individual task card
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_task, parent, false)
        return ViewHolder(view, completionListener)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // used to bind info to each task
        var taskDescr = mList.get(position).task
        when(mList.get(position).status) {
            ("Added") -> {
                holder.completion.setImageResource(R.drawable.added)
            }
            ("Started") -> {
                holder.completion.setImageResource(R.drawable.started)

            }
            ("Completed") -> {
                holder.completion.setImageResource(R.drawable.completed)

            }
        }
        holder.taskText.text = taskDescr
        holder.layout.tag = position
        holder.layout.setOnTouchListener(this)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    fun dayChange(newDay : ArrayList<TaskEntity>) {
        mList.clear()
        for(elem in newDay) {
            mList.add(elem)
        }
        notifyDataSetChanged()
    }

    fun removeAt(index: Int) {
        mList.removeAt(index)
        notifyItemRemoved(index)
    }

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val completion: ImageButton = itemView.findViewById(R.id.status)
        val taskText: TextView = itemView.findViewById(R.id.taskText)
        val layout: LinearLayout = itemView.findViewById(R.id.taskLayout)

        init{
            completion.setOnClickListener{
                listener.onClick(bindingAdapterPosition)
            }
        }

    }

    override fun onTouch(v: View?, p1: MotionEvent?): Boolean {
        when (p1!!.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = DragShadowBuilder(v)
                v!!.startDragAndDrop(data, shadowBuilder, v, 0)
                return true
            }
        }
        return false
    }


}
