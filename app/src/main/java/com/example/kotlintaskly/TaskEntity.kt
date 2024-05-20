package com.example.kotlintaskly

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskBacklog")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val task: String,
    val location: String,
    val date: String
)
