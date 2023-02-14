package com.example.tubes1pbd.ui.twibbon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TwibbonViewModel : ViewModel() {


    private val _twibbonSaveButtonText = MutableLiveData<String>().apply {
        value = "Save to Gallery"
    }
    private val _twibbonCaptureButtonText = MutableLiveData<String>().apply {
        value = "Capture a Photo"
    }

    fun toggleCaptureButtonText(isCaptured: Boolean) {
        if (isCaptured) {
            _twibbonCaptureButtonText.value = "Capture a Photo"
        } else {
            _twibbonCaptureButtonText.value = "Retake Photo"
        }
    }
    val twibbonCaptureButtonText: LiveData<String>
        get() = _twibbonCaptureButtonText

    val twibbonSaveButtonText: LiveData<String>
        get() = _twibbonSaveButtonText

}