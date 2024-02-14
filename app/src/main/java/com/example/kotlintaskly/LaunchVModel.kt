package com.example.kotlintaskly

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LaunchVModel(application: Application): AndroidViewModel(application) {
    // needed for the pause on splash (launch) animation
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()
    private var dataStore = BasicData(application)
    var pin = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var skip = MutableLiveData<String>()

    // immediately execute the splash delay
    init {
        viewModelScope.launch {
            var temp = dataStore.retrieve("pin")
            if(temp != null) {
                pin.value = temp!!
            }
            else {
                pin.value = "0000"
            }
            _isReady.value = true
        }
    }

    fun returnPin() : String? {
        return pin.value.toString()
    }

    fun data(item: String) {
        viewModelScope.launch {
            dataStore.set("pin", item)
            pin.value = item
        }
    }
    fun email(item: String) {
        email.value = item
    }
    fun skip(item: String) {
        skip.value = "ApplePie"
    }
}