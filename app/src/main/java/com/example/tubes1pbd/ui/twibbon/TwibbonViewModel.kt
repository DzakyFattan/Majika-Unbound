package com.example.tubes1pbd.ui.twibbon

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.icu.text.SimpleDateFormat
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tubes1pbd.R
import com.example.tubes1pbd.service.SingleEvent
import java.io.File
import java.util.*


class TwibbonViewModel(app: Application) : AndroidViewModel(app){

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
    private var _bitmapCamHolder = MutableLiveData<Bitmap> ()
    private var _bitmapTwibbonHolder = MutableLiveData<Bitmap> ()
    private var _bitmapCombined = MutableLiveData<Bitmap> ()
    private var _saveBitmapMessage = MutableLiveData<SingleEvent<String>>()
    val saveBitmapMessage: LiveData<SingleEvent<String>>
        get() = _saveBitmapMessage
    val bitmapTwibbonHolder: LiveData<Bitmap>
        get() = _bitmapTwibbonHolder
    val bitmapCombined: LiveData<Bitmap>
        get() = _bitmapCombined

    fun setBitmapCamHolder(bitmap: Bitmap) {
        _bitmapCamHolder.value = bitmap
        updateBitmapCombined()
    }
    fun setBitmapTwibbonHolder(bitmap: Bitmap) {
        _bitmapTwibbonHolder.value = bitmap
        updateBitmapCombined()
    }
    fun saveCombinedBitmap() {
        if (_bitmapCombined.value == null) {
            _saveBitmapMessage.value = SingleEvent("No image to save")
            return
        }
        try {
            val filename = SimpleDateFormat(FILENAME_FORMAT, Locale.UK).format(System.currentTimeMillis())
            val appname = getApplication<Application>().resources.getString(R.string.app_name)
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + appname)
            }
            val resolver = getApplication<Application>().contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val stream = resolver.openOutputStream(uri!!)
            _bitmapCombined.value!!.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            stream!!.close()
            _saveBitmapMessage.value = SingleEvent("Saved to gallery at ${Environment.DIRECTORY_PICTURES + File.separator + appname + File.separator + filename}")
        } catch (e: Exception) {
            Log.e("TwibbonViewModel", "saveCombinedBitmap: ", e)
            _saveBitmapMessage.value = SingleEvent("Failed to save to gallery")
        }
    }
    private fun updateBitmapCombined() {
        if (_bitmapCamHolder.value == null || _bitmapTwibbonHolder.value == null) return
        // get the smallest dimension for all bitmap
        val bitmap = _bitmapCamHolder.value
        val twibbon = _bitmapTwibbonHolder.value
        val result = Bitmap.createBitmap(twibbon!!.width, twibbon.height, bitmap!!.config)
        val canvas = Canvas(result)
        Log.d("TwibbonViewModel", "updateBitmapCombined: ${bitmap.width} ${bitmap.height} ${twibbon.width} ${twibbon.height}")
        canvas.drawBitmap(bitmap, Rect(
            0,
            0,
            bitmap.width,
            bitmap.height,
        ), Rect(
            0,
            0,
            twibbon.width,
            twibbon.height
        ), null)
        canvas.drawBitmap(twibbon, 0f, 0f, null)
        _bitmapCombined.value = result
    }
}