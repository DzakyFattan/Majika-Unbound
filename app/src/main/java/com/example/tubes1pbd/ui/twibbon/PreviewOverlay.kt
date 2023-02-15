package com.example.tubes1pbd.ui.twibbon

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class PreviewOverlay constructor(
    context: Context?,
    attributeSet: AttributeSet?,
) : View(context, attributeSet) {

    private var _bitmap: Bitmap? = null
    fun setBitmap(bitmap: Bitmap?) {
        _bitmap = bitmap
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(_bitmap!!, null, Rect(0, 0, width, height), null)
    }
}