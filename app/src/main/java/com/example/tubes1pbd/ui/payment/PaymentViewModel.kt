package com.example.tubes1pbd.ui.payment

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat.YUV_420_888
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Looper
import android.os.Handler
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.os.postDelayed
import androidx.lifecycle.*
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.models.DecodeResponse
import com.example.tubes1pbd.service.RestApi
import com.example.tubes1pbd.service.RestApiBuilder
import com.example.tubes1pbd.service.SingleEvent
import com.example.tubes1pbd.ui.keranjang.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class PaymentViewModel(private val db: MajikaRoomDatabase) : ViewModel() {
    private var _paymentStatus = MutableLiveData<String>()
    private var _paymentMessage = MutableLiveData<String>()
    private var _isPaymentSuccessful = MutableLiveData(false)
    private var _internalSuccessState = true
    private var postJob: Job? = null
    private val apiCall: RestApi = RestApiBuilder.getClient()
    val paymentStatus: LiveData<String>
        get() = _paymentStatus
    val paymentMessage: LiveData<String>
        get() = _paymentMessage
    val isPaymentSuccessful: LiveData<Boolean>
        get() = _isPaymentSuccessful


    fun postDebounced(qrcode: String) {
        if (postJob?.isActive == true || _isPaymentSuccessful.value == true) {
            return
        }
        postJob = viewModelScope.launch {
            if (!_internalSuccessState) return@launch
            _internalSuccessState = false
            _paymentStatus.value = "Loading"
            _paymentMessage.value = ""
            delay(1000)
            apiCall.sendQRDecode(qrcode).enqueue(object : retrofit2.Callback<DecodeResponse> {
                override fun onResponse(call: retrofit2.Call<DecodeResponse>, response: retrofit2.Response<DecodeResponse>) {
                    if (response.code() != 200) {
                        _paymentStatus.value = "Error"
                        _paymentMessage.value = "Kode QR tidak valid"
                        Handler(Looper.getMainLooper()).postDelayed({
                            _internalSuccessState = true
                            _isPaymentSuccessful.value = false
                        }, 2000)
                        return
                    }
                    val respbody = response.body()
                    if (respbody == null) {
                        _paymentStatus.value = "Error"
                        _paymentMessage.value = "Kode QR tidak valid"
                        Handler(Looper.getMainLooper()).postDelayed({
                            _internalSuccessState = true
                            _isPaymentSuccessful.value = false
                        }, 2000)
                        return
                    }
                    val status = respbody.status
                    Log.d("QRCode_Sent", status)
                    if (status == "SUCCESS") {
                        viewModelScope.launch (Dispatchers.IO) {
                            db.cartDao.deleteAllCartItem()
                        }
                        _paymentStatus.value = "Pembayaran Berhasil"
                        _paymentMessage.value = "Anda akan kembali dalam lima detik"
                        Handler(Looper.getMainLooper()).postDelayed({
                            _internalSuccessState = true
                            _isPaymentSuccessful.value = true
                        }, 5000)
                    } else {
                        _paymentStatus.value = "Pembayaran Gagal"
                        _paymentMessage.value = "Silakan coba lagi"
                        Handler(Looper.getMainLooper()).postDelayed({
                            _internalSuccessState = true
                            _isPaymentSuccessful.value = false
                        }, 2000)
                    }
                }
                override fun onFailure(call: retrofit2.Call<DecodeResponse>, t: Throwable) {
                    if (_isPaymentSuccessful.value == true) return
                    _paymentStatus.value = "Error"
                    _paymentMessage.value = "Something went wrong"
                    Handler(Looper.getMainLooper()).postDelayed({
                        _internalSuccessState = true
                        _isPaymentSuccessful.value = false
                    }, 2000)
                }
            })
        }
    }
    class Factory(private val database: MajikaRoomDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PaymentViewModel(database) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
