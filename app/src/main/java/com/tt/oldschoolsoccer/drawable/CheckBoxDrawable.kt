package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R

class CheckBoxDrawable (private val context: Context, private val size:Double, private val screenUnit:Double, private val black:Boolean): Drawable() {
    private val paint = Paint()
    private val lineWidth = 2*screenUnit/10

    override fun draw(canvas: Canvas) {
        val rect = RectF(0f,0f,size.toFloat(),size.toFloat())
        paint.style = Paint.Style.STROKE
        if(black){
            paint.color = ContextCompat.getColor(context, R.color.black)
        }else{
            paint.color = ContextCompat.getColor(context, R.color.icon_grey_medium)
        }


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