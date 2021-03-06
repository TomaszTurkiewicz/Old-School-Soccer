package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R

class ButtonGreyDrawable (private val context: Context, private val width:Double, private val height:Double, private val screenUnit:Double): Drawable() {
    private val paint = Paint()
    private val lineWidth = 2*screenUnit/10

    override fun draw(canvas: Canvas) {
        val rect = RectF(0f,0f,width.toFloat(),height.toFloat())
        paint.style = Paint.Style.STROKE
        paint.color = ContextCompat.getColor(context, R.color.icon_grey_buttons)
        paint.strokeWidth = lineWidth.toFloat()
        canvas.drawRect(rect,paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}