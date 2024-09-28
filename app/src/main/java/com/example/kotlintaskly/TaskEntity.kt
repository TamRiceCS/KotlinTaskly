package com.example.kotlintaskly
import androidx.room.Entity

@Entity(tableName = "taskBacklog", primaryKeys = ["task", "location", "date"])
data class TaskEntity(
    var task: String,
    var location: String,
    var date: String,
    var status: String
)
