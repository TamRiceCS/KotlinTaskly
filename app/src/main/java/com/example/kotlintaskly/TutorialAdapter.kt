package com.example.kotlintaskly

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TutorialAdapter(private var tutorialMsgs : ArrayList<String>, private var images: ArrayList<Int>) : RecyclerView.Adapter<TutorialAdapter.Pager2ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tutorial_page, parent, false))
    }

    override fun getItemCount(): Int {
        return tutorialMsgs.size
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.tutorialText.text = tutorialMsgs[position]
        holder.img.setImageResource(images[position])
    }

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.tutorialImage)
        val tutorialText: TextView = itemView.findViewById(R.id.tutorialText)
    }
}