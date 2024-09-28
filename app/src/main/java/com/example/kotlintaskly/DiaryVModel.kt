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

    fun fetch(status: String){
        viewModelScope.launch((Dispatchers.IO)) {
            if(status == "Published") {
                val pureResults = DiaryActivity.db.diaryDao().getListOfStatus(status)
                backendPublished = pureResults
                publishedEntries.postValue(pureResults)
            }
            if(status == "Drafted"){
                val pureResults = DiaryActivity.db.diaryDao().getListOfStatus(status)
                draftedEntries.postValue(pureResults)
            }
        }
    }


}