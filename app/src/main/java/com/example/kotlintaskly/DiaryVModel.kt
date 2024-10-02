package com.example.kotlintaskly

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryVModel(application: Application): AndroidViewModel(application) {
    var publishedEntries = MutableLiveData<List<DiaryEntity>>()
    var draftedEntries = MutableLiveData<List<DiaryEntity>>()
    var backendPublished : List<DiaryEntity>? = null
    var backendDrafted : List<DiaryEntity>? = null

    val basicDS = BasicData.getInstance(application)

    fun insertBacklog(base : DiaryEntity){
        viewModelScope.launch((Dispatchers.IO)) {
            DiaryActivity.db.diaryDao().insertEntry(base)
            fetch("Published")
        }
    }

    fun updateBacklog(oldTitle: String, newTitle: String, date: String, updateEntry: String) {
        viewModelScope.launch((Dispatchers.IO)) {
            DiaryActivity.db.diaryDao().updateStatus(updateEntry, newTitle, oldTitle, date)
            fetch("Published")
        }
    }

    fun deleteBacklog(title: String, date: String) {
        viewModelScope.launch((Dispatchers.IO)) {
            DiaryActivity.db.diaryDao().delete(title, date)
            fetch("Published")
            fetch("Drafted")
        }
    }

    fun clearDiary(){
        viewModelScope.launch((Dispatchers.IO)) {
            DiaryActivity.db.diaryDao().clearData()
        }
    }

    fun fetch(status: String){
        viewModelScope.launch((Dispatchers.IO)) {
            if(status == "Published") {
                val pureResults = DiaryActivity.db.diaryDao().getListOfStatus(status)
                backendPublished = pureResults
                publishedEntries.postValue(pureResults)
            }
            if(status == "Drafted"){
                val pureResults = DiaryActivity.db.diaryDao().getListOfStatus(status)
                backendDrafted = pureResults
                draftedEntries.postValue(pureResults)
            }
        }
    }


}