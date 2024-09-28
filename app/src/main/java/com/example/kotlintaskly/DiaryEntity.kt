package com.example.kotlintaskly

import androidx.room.Entity

@Entity(tableName = "diaryBacklog", primaryKeys = ["title", "date"])
data class DiaryEntity(
    var title: String,
    var entry : String,
    var date: String,
    var status: String,
)
