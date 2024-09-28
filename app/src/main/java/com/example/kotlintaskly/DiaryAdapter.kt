package com.example.kotlintaskly

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton

import android.widget.TextView


class DiaryAdapter(private var mList: ArrayList<DiaryEntity>) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_diary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = mList[position].title
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun populate(fetched : ArrayList<DiaryEntity>) {
        mList.clear()
        for(elem in fetched){
            mList.add(elem)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.title)
        val edit : ImageButton = itemView.findViewById(R.id.edit)
    }

}