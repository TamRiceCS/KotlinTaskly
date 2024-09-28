package com.example.kotlintaskly

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DiaryDAO {
    @Query("SELECT * FROM diaryBacklog where diaryBacklog.status = :status")
    fun getListOfStatus(status : String): List<DiaryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEntry(entry: DiaryEntity)

    @Query("delete from diaryBacklog where diaryBacklog.title = :title and diaryBacklog.date = :date")
    fun delete(title: String, date: String)

    @Query("update diaryBacklog set entry = :newEntry where diaryBacklog.title = :title and diaryBacklog.date = :date")
    fun updateStatus(newEntry: String, title: String, date: String)


}