package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.database.PointOnField

/**
 * drawing ball on the field
 */
class BallDrawable (private val context: Context, private val ball: PointOnField, private val screenUnit:Double):Drawable(){
    private val paint = Paint()
    private val lineWidth = screenUnit/10


    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color=ContextCompat.getColor(context, R.color.design_default_color_error)
        paint.strokeWidth=lineWidth.toFloat()

        canvas.drawCircle((ball.x*screenUnit).toFloat(), (ball.y*screenUnit).toFloat(), lineWidth.toFloat(),paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}