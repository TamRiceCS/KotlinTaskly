package com.example.kotlintaskly

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout

import android.widget.TextView


class DiaryAdapter(private var mList: ArrayList<DiaryEntity>) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {
    private lateinit var tappedBttn: onItemClickListener
    private lateinit var tappedCard: onItemClickListener


    interface onItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnBttnClickListener(listener: onItemClickListener){
        tappedBttn = listener
    }
    fun setOnCardClickListener(listener: onItemClickListener){
        tappedCard = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_diary, parent, false)
        return ViewHolder(view, tappedBttn, tappedCard)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = mList[position].title
        holder.card.tag = position
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

    class ViewHolder(itemView: View, listenerBttn: onItemClickListener, listenerCard: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.title)
        val card : LinearLayout = itemView.findViewById(R.id.diaryLayout)
        private val edit : ImageButton = itemView.findViewById(R.id.edit)

        init {
            edit.setOnClickListener{
                listenerBttn.onClick(bindingAdapterPosition)
            }
            card.setOnClickListener{
                listenerCard.onClick((card.tag as Int))
            }
        }


    }

}