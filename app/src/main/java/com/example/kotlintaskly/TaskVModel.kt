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
    var firstLimit = MutableLiveData<Int>(3)
    var secondLimit = MutableLiveData<Int>(3)
    var thirdLimit = MutableLiveData<Int>(3)
    var backendFirst : List<TaskEntity>? = null
    var backendSecond : List<TaskEntity>? = null
    var backendThird : List<TaskEntity>? = null
    val basicDS = BasicData.getInstance(application)

    fun insertBacklog(base : TaskEntity){
        viewModelScope.launch((Dispatchers.IO)) {
             TaskActivity.db.taskDao().insertTask(base)
             fetch(base.location, base.date)
        }
    }

    fun deleteBacklog(base : TaskEntity){
        viewModelScope.launch((Dispatchers.IO)) {
            TaskActivity.db.taskDao().delete(base.task,base.location, base.date)
        }
    }

    fun updateTaskStatus(base : TaskEntity){
        viewModelScope.launch((Dispatchers.IO)) {
            Log.d("Update Status", "{$base}, before anything")
            var updateStatus = "Added"
            when(base.status) {
                "Added" -> updateStatus = "Started"
                "Started" -> updateStatus = "Completed"
                "Completed" -> updateStatus = "Added"
            }
            Log.d("Update Status", "{$base}, will now be {$updateStatus}")
            TaskActivity.db.taskDao().updateStatus(base.task,base.location, base.date, updateStatus)
            fetch(base.location, base.date)
        }
    }

    fun updateLocation(base: TaskEntity, destination: String, orgin: String){
        viewModelScope.launch((Dispatchers.IO)) {
            TaskActivity.db.taskDao().updateLocation(base.task, orgin, destination, base.date)
            Log.d("Drag Event", "changed from " + orgin + " location to " + destination)
            fetch(destination, base.date)
            fetch(orgin, base.date)
        }
    }

    fun fetch(location: String, date : String) {
        viewModelScope.launch((Dispatchers.IO)) {
            when (location) {
                "First" -> {
                    var pureResults = TaskActivity.db.taskDao().getSection(location, date)
                    backendFirst = pureResults
                    firstTasks.postValue(pureResults)
                    Log.d("Drag Event", "changed First")
                }
                "Second" -> {
                    var pureResults = TaskActivity.db.taskDao().getSection(location, date)
                    backendSecond = pureResults
                    secondTasks.postValue(pureResults)
                    Log.d("Drag Event", "changed Second")
                }
                "Third" -> {
                    var pureResults = TaskActivity.db.taskDao().getSection(location, date)
                    backendThird = pureResults
                    thirdTasks.postValue(pureResults)
                    Log.d("Drag Event", "changed Second")
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

    fun getLimit(section: String){
        runBlocking {
            if(section == "FirstLimit") {
                var output: String? = basicDS.retrieve(section)
                if(output != null){
                    firstLimit.postValue(output!!.toInt())
                }
                else{
                    firstLimit.postValue(3)
                }
            }
            else if(section == "SecondLimit") {
                var output: String? = basicDS.retrieve(section)
                if(output != null){
                    secondLimit.postValue(output!!.toInt())
                }
                else{
                    secondLimit.postValue(3)
                }
            }
            else{
                var output : String? = basicDS.retrieve(section)
                if(output != null){
                    thirdLimit.postValue(output!!.toInt())
                }
                else{
                    thirdLimit.postValue(3)
                }
            }
        }
    }

    fun setLimit(value1: Int, value2: Int, value3: Int){
        runBlocking{
            basicDS.set("FirstLimit", value1.toString())
            basicDS.set("SecondLimit", value2.toString())
            basicDS.set("ThirdLimit", value3.toString())

            firstLimit.value = value1
            secondLimit.value = value2
            thirdLimit.value = value3
        }
        Log.d("checked", "Reflected in vModel spinner 1: " + firstLimit.value.toString())
        Log.d("checked", "Reflected in vModel spinner 2: " + secondLimit.value.toString())
        Log.d("checked", "Reflected in vModel spinner 3: " + thirdLimit.value.toString())
    }




}