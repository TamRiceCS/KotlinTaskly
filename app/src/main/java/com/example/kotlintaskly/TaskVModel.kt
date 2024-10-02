package com.example.kotlintaskly

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
    var addedCount = MutableLiveData<Int>(0)
    var startedCount = MutableLiveData<Int>(3)
    var completedCount = MutableLiveData<Int>(3)
    var countsReady = MutableLiveData<Boolean>(false)
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
            var updateStatus = "Added"
            when(base.status) {
                "Added" -> updateStatus = "Started"
                "Started" -> updateStatus = "Completed"
                "Completed" -> updateStatus = "Added"
            }
            TaskActivity.db.taskDao().updateStatus(base.task,base.location, base.date, updateStatus)
            fetch(base.location, base.date)
        }
    }

    fun updateLocation(base: TaskEntity, destination: String, orgin: String){
        viewModelScope.launch((Dispatchers.IO)) {
            TaskActivity.db.taskDao().updateLocation(base.task, orgin, destination, base.date)
            fetch(destination, base.date)
            fetch(orgin, base.date)
        }
    }

    fun countDayLocation(date : String){
        viewModelScope.launch((Dispatchers.IO)) {
            addedCount.postValue(TaskActivity.db.taskDao().count("Added", date))
            startedCount.postValue(TaskActivity.db.taskDao().count("Started", date))
            completedCount.postValue(TaskActivity.db.taskDao().count("Completed", date))

            if(countsReady.value == true) {
                countsReady.postValue(false)
            }
            else{
                countsReady.postValue(true)
            }



        }
    }

    fun fetch(location: String, date : String) {
        viewModelScope.launch((Dispatchers.IO)) {
            when (location) {
                "First" -> {
                    val pureResults = TaskActivity.db.taskDao().getSection(location, date)
                    backendFirst = pureResults
                    firstTasks.postValue(pureResults)
                }
                "Second" -> {
                    val pureResults = TaskActivity.db.taskDao().getSection(location, date)
                    backendSecond = pureResults
                    secondTasks.postValue(pureResults)
                }
                "Third" -> {
                    val pureResults = TaskActivity.db.taskDao().getSection(location, date)
                    backendThird = pureResults
                    thirdTasks.postValue(pureResults)
                }
            }
        }
    }

    fun setPin(item: String) {
        // when data is being set, we need to wait until this code executes, continuing can cause unexpected behavior
        runBlocking {
            basicDS.set("pin", item)
        }
    }

    fun getLimit(section: String){
        runBlocking {
            if(section == "FirstLimit") {
                val output: String? = basicDS.retrieve(section)
                if(output != null){
                    firstLimit.postValue(output!!.toInt())
                }
                else{
                    firstLimit.postValue(3)
                }
            }
            else if(section == "SecondLimit") {
                val output: String? = basicDS.retrieve(section)
                if(output != null){
                    secondLimit.postValue(output!!.toInt())
                }
                else{
                    secondLimit.postValue(3)
                }
            }
            else{
                val output : String? = basicDS.retrieve(section)
                if(output != null){
                    thirdLimit.postValue(output!!.toInt())
                }
                else{
                    thirdLimit.postValue(3)
                }
            }
        }
    }

    fun clearTasks(){
        viewModelScope.launch((Dispatchers.IO)) {
            TaskActivity.db.taskDao().clearData()
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
    }




}