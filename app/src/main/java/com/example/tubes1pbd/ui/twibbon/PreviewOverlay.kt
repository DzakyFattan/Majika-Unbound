package com.example.tubes1pbd.ui.twibbon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.example.tubes1pbd.R

class PreviewOverlay constructor(
    context: Context?,
    attributeSet: AttributeSet?
) : View(context, attributeSet) {
    private var mImage: Drawable
    init {
        mImage = AppCompatResources.getDrawable(context!!, R.drawable.twibex)!!
    }
    override fun onDraw(canvas: Canvas) {
        // set bound with canvas clip bounds
        mImage.bounds = canvas.clipBounds
        // draw image
        mImage.draw(canvas)
    }
}