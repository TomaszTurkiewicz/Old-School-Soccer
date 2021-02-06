package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.GameField
import com.tt.oldschoolsoccer.classes.Static

class MovesHardDrawable(private val context: Context, private val field: GameField, private val screenUnit:Double): Drawable() {

    private val paint = Paint()
    private val lineWidth = screenUnit/15

    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineWidth.toFloat()

        paint.color = ContextCompat.getColor(context, R.color.test)
        val path3 = Path()
        for(i in 0..12) {
            for (j in 0..20) {

                if (field.field[i][j].moveUp== Static.MOVE_AVAILABLE) {
                    path3.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path3.lineTo((field.field[i][j - 1].x * screenUnit).toFloat(), (field.field[i][j - 1].y * screenUnit).toFloat())
                }

                if (field.field[i][j].moveUpRight== Static.MOVE_AVAILABLE) {
                    path3.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path3.lineTo((field.field[i + 1][j - 1].x * screenUnit).toFloat(), (field.field[i + 1][j - 1].y * screenUnit).toFloat())
                }

                if (field.field[i][j].moveRight== Static.MOVE_AVAILABLE) {
                    path3.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path3.lineTo((field.field[i + 1][j].x * screenUnit).toFloat(), (field.field[i + 1][j].y * screenUnit).toFloat())
                }

                if (field.field[i][j].moveDownRight== Static.MOVE_AVAILABLE) {
                    path3.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path3.lineTo((field.field[i + 1][j + 1].x * screenUnit).toFloat(), (field.field[i + 1][j + 1].y * screenUnit).toFloat())
                }
            }
        }
        canvas.drawPath(path3,paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}