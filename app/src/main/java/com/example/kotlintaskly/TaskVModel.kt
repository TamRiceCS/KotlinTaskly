package com.example.kotlintaskly

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TaskVModel(application: Application): AndroidViewModel(application) {
    val basicDS = BasicData(application)
    var morningTasks = MutableLiveData<List<TaskEntity>>()
    var afternoonTasks = MutableLiveData<List<TaskEntity>>()
    var eveningTasks = MutableLiveData<List<TaskEntity>>()

    fun updateBacklog(base : TaskData){
        viewModelScope.launch((Dispatchers.IO)) {
            var sqlBackend = TaskEntity(task = base.task, location = base.location, date = base.date)
            Log.d("Miku", sqlBackend.id.toString() + " " + base.task)
            TaskActivity.db.taskDao().insertTask(sqlBackend)
        }
    }


}