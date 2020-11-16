package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.EasyGameField

class MovesEasyDrawable(private val context: Context, private val field:EasyGameField, private val screenUnit:Double):Drawable() {
    private val paint = Paint()
    private val lineWidth = screenUnit/15



    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.strokeWidth = lineWidth.toFloat()

        val path = Path()

        for(i in 0..8){
            for(j in 0..12){
                if(field.field[i][j].moveUp!=null){
                    if(field.field[i][j].moveUp!!){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i][j-1].x*screenUnit).toFloat(), (field.field[i][j-1].y*screenUnit).toFloat())
                    }
                }
                if(field.field[i][j].moveUpRight!=null){
                    if(field.field[i][j].moveUpRight!!){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i+1][j-1].x*screenUnit).toFloat(), (field.field[i+1][j-1].y*screenUnit).toFloat())
                    }
                }
                if(field.field[i][j].moveRight!=null){
                    if(field.field[i][j].moveRight!!){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i+1][j].x*screenUnit).toFloat(), (field.field[i+1][j].y*screenUnit).toFloat())
                    }
                }
                if(field.field[i][j].moveDownRight!=null){
                    if(field.field[i][j].moveDownRight!!){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i+1][j+1].x*screenUnit).toFloat(), (field.field[i+1][j+1].y*screenUnit).toFloat())
                    }
                }
                if(field.field[i][j].moveDown!=null){
                    if(field.field[i][j].moveDown!!){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i][j+1].x*screenUnit).toFloat(), (field.field[i][j+1].y*screenUnit).toFloat())
                    }
                }
                if(field.field[i][j].moveDownLeft!=null){
                    if(field.field[i][j].moveDownLeft!!){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i-1][j+1].x*screenUnit).toFloat(), (field.field[i-1][j+1].y*screenUnit).toFloat())
                    }
                }
                if(field.field[i][j].moveLeft!=null){
                    if(field.field[i][j].moveLeft!!){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i-1][j].x*screenUnit).toFloat(), (field.field[i-1][j].y*screenUnit).toFloat())
                    }
                }
                if(field.field[i][j].moveUpLeft!=null){
                    if(field.field[i][j].moveUpLeft!!){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i-1][j-1].x*screenUnit).toFloat(), (field.field[i-1][j-1].y*screenUnit).toFloat())
                    }
                }
            }
        }

        canvas.drawPath(path,paint)




    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

}