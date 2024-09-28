package com.example.kotlintaskly

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DiaryEntity::class], version = 1)
abstract class DiaryDatabase: RoomDatabase() {
    abstract fun diaryDao(): DiaryDAO

}