package com.example.kotlintaskly

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private var mList: ArrayList<TaskEntity>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>(){


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
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // add items to list
    fun addAndInform(item : TaskEntity, position: Int) {
        mList.add(position, item)
        notifyItemInserted(position)
    }

    fun dayChange(newDay : ArrayList<TaskEntity>) {
        mList.clear()
        for(elem in newDay) {
            mList.add(elem)
        }
        notifyDataSetChanged()
    }

    fun complete(index: Int) {
        mList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun modify(index: Int) {
        when(mList[index].status) {
            "Added" -> mList[index].status = "Started"
            "Started" -> mList[index].status = "Completed"
            "Completed" -> mList[index].status = "Added"
        }
        Log.d("Update Status", "In the Adapter it is " + mList[index].status)
        notifyItemChanged(index)

    }


    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val notifBell: ImageButton = itemView.findViewById(R.id.setReminder)
        val completion: ImageButton = itemView.findViewById(R.id.status)
        val taskText: TextView = itemView.findViewById(R.id.taskText)

        init{
            completion.setOnClickListener{
                listener.onClick(bindingAdapterPosition)
            }
        }

    }
}
