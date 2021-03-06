package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.GameField
import com.tt.oldschoolsoccer.classes.Static

/**
 * drawing lines where moves were done
 */
class MovesEasyDrawable(private val context: Context, private val field:GameField, private val screenUnit:Double, private val ball:Point):Drawable() {
    private val paint = Paint()
    private var lineWidth = screenUnit/15



    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineWidth.toFloat()

        paint.color = ContextCompat.getColor(context, R.color.test)
        val path3 = Path()
        for(i in 0..Static.EASY_SIZE_WIDTH_INDEX) {
            for (j in 0..Static.EASY_SIZE_HEIGHT_INDEX) {

                if (field.field[i][j].moveUp==Static.MOVE_CHECKING) {
                    path3.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path3.lineTo((field.field[i][j - 1].x * screenUnit).toFloat(), (field.field[i][j - 1].y * screenUnit).toFloat())
                }

                if (field.field[i][j].moveUpRight==Static.MOVE_CHECKING) {
                    path3.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path3.lineTo((field.field[i + 1][j - 1].x * screenUnit).toFloat(), (field.field[i + 1][j - 1].y * screenUnit).toFloat())
                }

                if (field.field[i][j].moveRight==Static.MOVE_CHECKING) {
                    path3.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path3.lineTo((field.field[i + 1][j].x * screenUnit).toFloat(), (field.field[i + 1][j].y * screenUnit).toFloat())
                }

                if (field.field[i][j].moveDownRight==Static.MOVE_CHECKING) {
                    path3.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path3.lineTo((field.field[i + 1][j + 1].x * screenUnit).toFloat(), (field.field[i + 1][j + 1].y * screenUnit).toFloat())
                }
            }
        }
        canvas.drawPath(path3,paint)

        paint.color = ContextCompat.getColor(context, R.color.win)
        paint.strokeWidth = (screenUnit/5).toFloat()
        val path4 = Path()
        for(i in 0..Static.EASY_SIZE_WIDTH_INDEX) {
            for (j in 0..Static.EASY_SIZE_HEIGHT_INDEX) {

                if (field.field[i][j].moveUp==Static.MOVE_BEST) {
                    path4.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path4.lineTo((field.field[i][j - 1].x * screenUnit).toFloat(), (field.field[i][j - 1].y * screenUnit).toFloat())
                }

                if (field.field[i][j].moveUpRight==Static.MOVE_BEST) {
                    path4.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path4.lineTo((field.field[i + 1][j - 1].x * screenUnit).toFloat(), (field.field[i + 1][j - 1].y * screenUnit).toFloat())
                }

                if (field.field[i][j].moveRight==Static.MOVE_BEST) {
                    path4.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path4.lineTo((field.field[i + 1][j].x * screenUnit).toFloat(), (field.field[i + 1][j].y * screenUnit).toFloat())
                }

                if (field.field[i][j].moveDownRight==Static.MOVE_BEST) {
                    path4.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                    path4.lineTo((field.field[i + 1][j + 1].x * screenUnit).toFloat(), (field.field[i + 1][j + 1].y * screenUnit).toFloat())
                }
            }
        }
        canvas.drawPath(path4,paint)

        paint.strokeWidth = lineWidth.toFloat()
        paint.color = ContextCompat.getColor(context, R.color.black)
        val path = Path()
        for(i in 0..Static.EASY_SIZE_WIDTH_INDEX) {
            for (j in 0..Static.EASY_SIZE_HEIGHT_INDEX) {

                    if(field.field[i][j].moveUp==Static.MOVE_DONE_BY_ME){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i][j-1].x*screenUnit).toFloat(), (field.field[i][j-1].y*screenUnit).toFloat())
                    }

                    if(field.field[i][j].moveUpRight==Static.MOVE_DONE_BY_ME){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i+1][j-1].x*screenUnit).toFloat(), (field.field[i+1][j-1].y*screenUnit).toFloat())
                    }

                    if(field.field[i][j].moveRight==Static.MOVE_DONE_BY_ME){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i+1][j].x*screenUnit).toFloat(), (field.field[i+1][j].y*screenUnit).toFloat())
                    }

                    if(field.field[i][j].moveDownRight==Static.MOVE_DONE_BY_ME){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i+1][j+1].x*screenUnit).toFloat(), (field.field[i+1][j+1].y*screenUnit).toFloat())
                    }
            }
        }
        canvas.drawPath(path,paint)

        paint.color = ContextCompat.getColor(context, R.color.lost)
        val path2 = Path()
        for(i in 0..Static.EASY_SIZE_WIDTH_INDEX) {
            for (j in 0..Static.EASY_SIZE_HEIGHT_INDEX) {

                    if (field.field[i][j].moveUp==Static.MOVE_DONE_BY_PHONE) {
                        path2.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                        path2.lineTo((field.field[i][j - 1].x * screenUnit).toFloat(), (field.field[i][j - 1].y * screenUnit).toFloat())
                    }

                    if (field.field[i][j].moveUpRight==Static.MOVE_DONE_BY_PHONE) {
                        path2.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                        path2.lineTo((field.field[i + 1][j - 1].x * screenUnit).toFloat(), (field.field[i + 1][j - 1].y * screenUnit).toFloat())
                    }

                    if (field.field[i][j].moveRight==Static.MOVE_DONE_BY_PHONE) {
                        path2.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                        path2.lineTo((field.field[i + 1][j].x * screenUnit).toFloat(), (field.field[i + 1][j].y * screenUnit).toFloat())
                    }

                    if (field.field[i][j].moveDownRight==Static.MOVE_DONE_BY_PHONE) {
                        path2.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                        path2.lineTo((field.field[i + 1][j + 1].x * screenUnit).toFloat(), (field.field[i + 1][j + 1].y * screenUnit).toFloat())
                    }
            }
        }
        canvas.drawPath(path2,paint)


        lineWidth = screenUnit/10
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color=ContextCompat.getColor(context, R.color.design_default_color_error)
        paint.strokeWidth=lineWidth.toFloat()

        canvas.drawCircle((ball.x*screenUnit).toFloat(), (ball.y*screenUnit).toFloat(), lineWidth.toFloat(),paint)

    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

}