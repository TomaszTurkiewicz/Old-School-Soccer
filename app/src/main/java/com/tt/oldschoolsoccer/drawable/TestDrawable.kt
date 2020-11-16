package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.PointOnField

class TestDrawable(private val context: Context, private val currentPoint:PointOnField, private val screenUnit:Double):Drawable() {
    private val paint = Paint()
    private val lineWidth:Double = screenUnit/10


    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = ContextCompat.getColor(context, R.color.test)
        paint.strokeWidth = lineWidth.toFloat()

        canvas.drawCircle((currentPoint.x*screenUnit).toFloat(), (currentPoint.y*screenUnit).toFloat(), lineWidth.toFloat(),paint)

        val path = Path()

        path.moveTo((currentPoint.x*screenUnit).toFloat(), (currentPoint.y*screenUnit).toFloat())

        if(currentPoint.moveUp!=null){
            path.lineTo((currentPoint.x*screenUnit).toFloat(), ((currentPoint.y-1)*screenUnit).toFloat())
            path.moveTo((currentPoint.x*screenUnit).toFloat(), (currentPoint.y*screenUnit).toFloat())
        }
        if(currentPoint.moveUpRight!=null){
            path.lineTo(((currentPoint.x+1)*screenUnit).toFloat(), ((currentPoint.y-1)*screenUnit).toFloat())
            path.moveTo((currentPoint.x*screenUnit).toFloat(), (currentPoint.y*screenUnit).toFloat())
        }
        if(currentPoint.moveRight!=null){
            path.lineTo(((currentPoint.x+1)*screenUnit).toFloat(), ((currentPoint.y)*screenUnit).toFloat())
            path.moveTo((currentPoint.x*screenUnit).toFloat(), (currentPoint.y*screenUnit).toFloat())
        }
        if(currentPoint.moveDownRight!=null){
            path.lineTo(((currentPoint.x+1)*screenUnit).toFloat(), ((currentPoint.y+1)*screenUnit).toFloat())
            path.moveTo((currentPoint.x*screenUnit).toFloat(), (currentPoint.y*screenUnit).toFloat())
        }
        if(currentPoint.moveDown!=null){
            path.lineTo((currentPoint.x*screenUnit).toFloat(), ((currentPoint.y+1)*screenUnit).toFloat())
            path.moveTo((currentPoint.x*screenUnit).toFloat(), (currentPoint.y*screenUnit).toFloat())
        }
        if(currentPoint.moveDownLeft!=null){
            path.lineTo(((currentPoint.x-1)*screenUnit).toFloat(), ((currentPoint.y+1)*screenUnit).toFloat())
            path.moveTo((currentPoint.x*screenUnit).toFloat(), (currentPoint.y*screenUnit).toFloat())
        }
        if(currentPoint.moveLeft!=null){
            path.lineTo(((currentPoint.x-1)*screenUnit).toFloat(), ((currentPoint.y)*screenUnit).toFloat())
            path.moveTo((currentPoint.x*screenUnit).toFloat(), (currentPoint.y*screenUnit).toFloat())
        }
        if(currentPoint.moveUpLeft!=null){
            path.lineTo(((currentPoint.x-1)*screenUnit).toFloat(), ((currentPoint.y-1)*screenUnit).toFloat())
            path.moveTo((currentPoint.x*screenUnit).toFloat(), (currentPoint.y*screenUnit).toFloat())
        }


        canvas.drawPath(path,paint)




    }

    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter=colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}