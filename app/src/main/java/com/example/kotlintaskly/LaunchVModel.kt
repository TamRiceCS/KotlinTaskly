package com.example.kotlintaskly

import android.app.Application
import android.util.Log
import androidx.datastore.dataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LaunchVModel(application: Application): AndroidViewModel(application) {
    // needed for the pause on splash (launch) animation
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()
    val basicDS = BasicData(application)
    var pin = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var skip = MutableLiveData<String>()

    // immediately execute the splash delay
    init {
        Log.d("Run Order", "Initializing ViewModel")
        viewModelScope.launch {
            runBlocking {
                var fact = basicDS.retrieve("pin")
                if(fact != null) {
                    Log.d("Run Order", "Pin Check is true")
                    pin.value = basicDS.retrieve("pin")!!
                    _isReady.value = true
                }
                else {
                    Log.d("Run Order", "Pin Check is false")
                    data("tutorial")
                    _isReady.value = true
                }
            }
        }
        Log.d("Run Order", "Done Initializing ViewModel")
    }

    fun returnPin() : String? {
        return pin.value
    }

    fun data(item: String) {
        runBlocking {
            Log.d("Run Order", "START Updating Data: " + basicDS.retrieve("pin"))
            basicDS.set("pin", item)
            pin.value = basicDS.retrieve("pin")
            Log.d("Run Order", "DURING Updating Data: " + basicDS.retrieve("pin"))


            Log.d("Run Order", "END Updating Data: " + basicDS.retrieve("pin"))
        }
    }
    fun email(item: String) {
        email.value = item
    }
    fun skip(item: String) {
        skip.value = "ApplePie"
    }
}