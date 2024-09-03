package com.example.kotlintaskly

import android.app.Application
import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TaskVModel(application: Application): AndroidViewModel(application) {
    var morningTasks = MutableLiveData<List<TaskEntity>>()
    var afternoonTasks = MutableLiveData<List<TaskEntity>>()
    var eveningTasks = MutableLiveData<List<TaskEntity>>()
    var morningLimit = 3
    var afternoonLimit = 3
    var eveningLimit = 3
    lateinit var backendMorning : List<TaskEntity>
    lateinit var backendAfternoon : List<TaskEntity>
    lateinit var backendEvening : List<TaskEntity>
    val basicDS = BasicData.getInstance(application)

    fun updateBacklog(base : TaskEntity){
        viewModelScope.launch((Dispatchers.IO)) {
            TaskActivity.db.taskDao().insertTask(base)
        }
    }

    fun fetch(location: String, date : String) {
        viewModelScope.launch((Dispatchers.IO)) {
            if(location == "Morning") {
                var pureResults = TaskActivity.db.taskDao().getSection(location, date)
                backendMorning = pureResults
                morningTasks.postValue(pureResults)
            }
            else if(location == "Afternoon") {
                var pureResults = TaskActivity.db.taskDao().getSection(location, date)
                backendAfternoon = pureResults
                afternoonTasks.postValue(pureResults)
            }
            else if (location == "Evening"){
                var pureResults = TaskActivity.db.taskDao().getSection(location, date)
                backendEvening = pureResults
                eveningTasks.postValue(pureResults)
                Log.d("Run Order", "Evening")
            }
        }
    }

    fun setPin(item: String) {
        // when data is being set, we need to wait until this code executes, continuing can cause unexpected behavior
        runBlocking {
            basicDS.set("pin", item)
            var message = basicDS.retrieve("pin")!!
            if(message != null) {
                Log.d("Run Order", "Ran reset mode code...$item")
            }
        }
    }




}