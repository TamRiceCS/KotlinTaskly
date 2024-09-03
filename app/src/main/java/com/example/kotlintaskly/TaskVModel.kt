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
    var firstTasks = MutableLiveData<List<TaskEntity>>()
    var secondTasks = MutableLiveData<List<TaskEntity>>()
    var thirdTasks = MutableLiveData<List<TaskEntity>>()
    var firstLimit = 3
    var secondLimit = 3
    var thirdLimit = 3
    lateinit var backendFirst : List<TaskEntity>
    lateinit var backendSecond : List<TaskEntity>
    lateinit var backendThird : List<TaskEntity>
    val basicDS = BasicData.getInstance(application)

    fun updateBacklog(base : TaskEntity){
        viewModelScope.launch((Dispatchers.IO)) {
            TaskActivity.db.taskDao().insertTask(base)
        }
    }

    fun fetch(location: String, date : String) {
        viewModelScope.launch((Dispatchers.IO)) {
            when (location) {
                "First" -> {
                    var pureResults = TaskActivity.db.taskDao().getSection(location, date)
                    backendFirst = pureResults
                    firstTasks.postValue(pureResults)
                }
                "Second" -> {
                    var pureResults = TaskActivity.db.taskDao().getSection(location, date)
                    backendSecond = pureResults
                    secondTasks.postValue(pureResults)
                }
                "Third" -> {
                    var pureResults = TaskActivity.db.taskDao().getSection(location, date)
                    backendThird = pureResults
                    thirdTasks.postValue(pureResults)
                    Log.d("Run Order", "third")
                }
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