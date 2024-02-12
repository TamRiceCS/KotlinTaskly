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
    var pin = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var skip = MutableLiveData<String>()

    // immediately execute the splash delay
    init {
        viewModelScope.launch {
            //delay(3000L)
            _isReady.value = true
        }
    }

    private val repository = BasicData(application)

    fun readPin(key: String, value: String) {
        viewModelScope.launch{
            repository.read(key, value)
        }
    }

    fun updatePin(key : String) {
        viewModelScope.launch{
            pin.value = repository.retreive(key)
        }
    }


    fun data(item: String) {
        pin.value = item
    }
    fun email(item: String) {
        email.value = item
    }
    fun skip(item: String) {
        skip.value = "ApplePie"
    }
}