package com.example.kotlintaskly

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDAO {
    @Query("SELECT * FROM taskBacklog where taskBackLog.location = :location and taskBackLog.date = :date")
    fun getSection(location : String, date : String): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTask(task: TaskEntity)

    @Query("delete from taskBacklog where taskBacklog.task = :task and taskBacklog.location = :location and taskBacklog.date = :date")
    fun delete(task: String, location: String, date: String)

    @Query("update taskBacklog set status = :status where taskBacklog.task = :task and taskBacklog.location = :location and taskBacklog.date = :date")
    fun update(task: String, location: String, date: String, status:String)
}