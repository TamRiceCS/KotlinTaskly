package com.example.kotlintaskly

import androidx.room.Dao
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
    fun updateStatus(task: String, location: String, date: String, status:String)

    @Query("update taskBacklog set location = :newLocation where taskBacklog.task = :task and taskBacklog.location = :oldLocation and taskBacklog.date = :date")
    fun updateLocation(task: String, oldLocation: String, newLocation: String, date: String)


}