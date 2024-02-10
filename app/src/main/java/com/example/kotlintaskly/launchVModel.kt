package com.example.kotlintaskly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class launchVModel: ViewModel() {
    var pin = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var skip = MutableLiveData<String>()

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