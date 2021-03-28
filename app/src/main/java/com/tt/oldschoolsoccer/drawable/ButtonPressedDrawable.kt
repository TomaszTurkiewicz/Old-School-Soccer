package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R

class ButtonPressedDrawable (private val context: Context, private val width:Double, private val height:Double, private val screenUnit:Double): Drawable() {
    private val paint = Paint()
    private val lineWidth = 2*screenUnit/10

    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.strokeWidth = lineWidth.toFloat()

        val path = Path()
        path.moveTo(0F, height.toFloat())
        path.lineTo(0F,0F)
        path.lineTo(width.toFloat(),0F)
        path.lineTo(width.toFloat(),height.toFloat())

        canvas.drawPath(path,paint)

    }

    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}