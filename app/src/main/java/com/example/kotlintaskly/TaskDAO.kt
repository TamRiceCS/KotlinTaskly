package com.example.kotlintaskly

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDAO {
    @Query("SELECT * FROM taskBacklog where taskBackLog.location = :location and taskBackLog.date = :date")
    fun getSection(location : String, date : String): List<TaskEntity>

    @Insert
    fun insertTask(vararg task: TaskEntity)

    @Delete
    fun delete(task: TaskEntity)
}