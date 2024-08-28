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
    private val basicDS = BasicData.getInstance(application)
    val isReady = _isReady.asStateFlow()
    var pin = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var skip = MutableLiveData<String>()

    // immediately execute the splash delay
    init {
        viewModelScope.launch {
            // CRITICAL that this executes first, we need to know if a pin is in data otherwise launch will default to null
            runBlocking {
                val fact = basicDS.retrieve("pin")
                if(fact != null) {
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
    }

    fun returnPin() : String? {
        return pin.value
    }

    fun data(item: String) {
        // when data is being set, we need to wait until this code executes, continuing can cause unexpected behavior
        runBlocking {
            // set backend data as pin, then retrieve this set data and place in this pin data member
            basicDS.set("pin", item)
            pin.value = basicDS.retrieve("pin")
        }
    }
}