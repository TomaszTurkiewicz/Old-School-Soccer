package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.GameField
import com.tt.oldschoolsoccer.classes.Static

class MovesHardUpSideDownDrawable(private val context: Context, private val field: GameField, private val screenUnit:Double, private val ball: Point):Drawable() {
    private val paint = Paint()
    private var lineWidth = screenUnit/15


    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineWidth.toFloat()

        paint.color = ContextCompat.getColor(context, R.color.black)
        val path = Path()
        for(i in 0..Static.HARD_SIZE_WIDTH_INDEX) {
            for (j in 0..Static.HARD_SIZE_HEIGHT_INDEX) {

                if(field.field[i][j].moveUp== Static.MOVE_DONE_BY_ME){
                    path.moveTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i][j].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i][j].y)*screenUnit).toFloat())
                    path.lineTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i][j-1].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i][j-1].y)*screenUnit).toFloat())
                }

                if(field.field[i][j].moveUpRight== Static.MOVE_DONE_BY_ME){
                    path.moveTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i][j].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i][j].y)*screenUnit).toFloat())
                    path.lineTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i+1][j-1].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i+1][j-1].y)*screenUnit).toFloat())
                }

                if(field.field[i][j].moveRight== Static.MOVE_DONE_BY_ME){
                    path.moveTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i][j].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i][j].y)*screenUnit).toFloat())
                    path.lineTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i+1][j].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i+1][j].y)*screenUnit).toFloat())
                }

                if(field.field[i][j].moveDownRight== Static.MOVE_DONE_BY_ME){
                    path.moveTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i][j].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i][j].y)*screenUnit).toFloat())
                    path.lineTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i+1][j+1].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i+1][j+1].y)*screenUnit).toFloat())
                }
            }
        }
        canvas.drawPath(path,paint)


        paint.color = ContextCompat.getColor(context, R.color.lost)
        val path2 = Path()
        for(i in 0..Static.HARD_SIZE_WIDTH_INDEX) {
            for (j in 0..Static.HARD_SIZE_HEIGHT_INDEX) {

                if(field.field[i][j].moveUp== Static.MOVE_DONE_BY_PHONE){
                    path2.moveTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i][j].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i][j].y)*screenUnit).toFloat())
                    path2.lineTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i][j-1].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i][j-1].y)*screenUnit).toFloat())
                }

                if(field.field[i][j].moveUpRight== Static.MOVE_DONE_BY_PHONE){
                    path2.moveTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i][j].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i][j].y)*screenUnit).toFloat())
                    path2.lineTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i+1][j-1].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i+1][j-1].y)*screenUnit).toFloat())
                }

                if(field.field[i][j].moveRight== Static.MOVE_DONE_BY_PHONE){
                    path2.moveTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i][j].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i][j].y)*screenUnit).toFloat())
                    path2.lineTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i+1][j].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i+1][j].y)*screenUnit).toFloat())
                }

                if(field.field[i][j].moveDownRight== Static.MOVE_DONE_BY_PHONE){
                    path2.moveTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i][j].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i][j].y)*screenUnit).toFloat())
                    path2.lineTo(((Static.HARD_SIZE_WIDTH_INDEX-field.field[i+1][j+1].x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-field.field[i+1][j+1].y)*screenUnit).toFloat())
                }
            }
        }
        canvas.drawPath(path2,paint)


        lineWidth = screenUnit/10

        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color=ContextCompat.getColor(context, R.color.design_default_color_error)
        paint.strokeWidth=lineWidth.toFloat()

        canvas.drawCircle(((Static.HARD_SIZE_WIDTH_INDEX-ball.x)*screenUnit).toFloat(), ((Static.HARD_SIZE_HEIGHT_INDEX-ball.y)*screenUnit).toFloat(), lineWidth.toFloat(),paint)

    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}