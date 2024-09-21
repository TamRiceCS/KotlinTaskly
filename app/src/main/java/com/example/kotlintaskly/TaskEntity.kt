package com.example.kotlintaskly

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskBacklog", primaryKeys = ["task", "location", "date"])
data class TaskEntity(
    var task: String,
    var location: String,
    var date: String,
    var status: String
)
