package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.inspector.StaticInspectionCompanionProvider
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R
import com.tt.oldschoolsoccer.classes.EasyGameField
import com.tt.oldschoolsoccer.classes.Static

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
                if(field.field[i][j].moveUp.moveDirection!=null){
                    if(field.field[i][j].moveUp.moveDirection!! and (field.field[i][j].moveUp.playerMove==Static.PLAYER) ){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i][j-1].x*screenUnit).toFloat(), (field.field[i][j-1].y*screenUnit).toFloat())
                    }
                }
                if(field.field[i][j].moveUpRight.moveDirection!=null){
                    if(field.field[i][j].moveUpRight.moveDirection!! and (field.field[i][j].moveUpRight.playerMove==Static.PLAYER)){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i+1][j-1].x*screenUnit).toFloat(), (field.field[i+1][j-1].y*screenUnit).toFloat())
                    }
                }
                if(field.field[i][j].moveRight.moveDirection!=null){
                    if(field.field[i][j].moveRight.moveDirection!! and (field.field[i][j].moveRight.playerMove==Static.PLAYER)){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i+1][j].x*screenUnit).toFloat(), (field.field[i+1][j].y*screenUnit).toFloat())
                    }
                }
                if(field.field[i][j].moveDownRight.moveDirection!=null){
                    if(field.field[i][j].moveDownRight.moveDirection!! and (field.field[i][j].moveDownRight.playerMove==Static.PLAYER)){
                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
                        path.lineTo((field.field[i+1][j+1].x*screenUnit).toFloat(), (field.field[i+1][j+1].y*screenUnit).toFloat())
                    }
                }
//                if(field.field[i][j].moveDown.moveDirection!=null){
//                    if(field.field[i][j].moveDown.moveDirection!!){
//                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
//                        path.lineTo((field.field[i][j+1].x*screenUnit).toFloat(), (field.field[i][j+1].y*screenUnit).toFloat())
//                    }
//                }
//                if(field.field[i][j].moveDownLeft.moveDirection!=null){
//                    if(field.field[i][j].moveDownLeft.moveDirection!!){
//                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
//                        path.lineTo((field.field[i-1][j+1].x*screenUnit).toFloat(), (field.field[i-1][j+1].y*screenUnit).toFloat())
//                    }
//                }
//                if(field.field[i][j].moveLeft.moveDirection!=null){
//                    if(field.field[i][j].moveLeft.moveDirection!!){
//                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
//                        path.lineTo((field.field[i-1][j].x*screenUnit).toFloat(), (field.field[i-1][j].y*screenUnit).toFloat())
//                    }
//                }
//                if(field.field[i][j].moveUpLeft.moveDirection!=null){
//                    if(field.field[i][j].moveUpLeft.moveDirection!!){
//                        path.moveTo((field.field[i][j].x*screenUnit).toFloat(), (field.field[i][j].y*screenUnit).toFloat())
//                        path.lineTo((field.field[i-1][j-1].x*screenUnit).toFloat(), (field.field[i-1][j-1].y*screenUnit).toFloat())
//                    }
//                }
            }
        }

        canvas.drawPath(path,paint)

        paint.color = ContextCompat.getColor(context, R.color.lost)
        val path2 = Path()
        for(i in 0..8) {
            for (j in 0..12) {
                if (field.field[i][j].moveUp.moveDirection != null) {
                    if (field.field[i][j].moveUp.moveDirection!! and (field.field[i][j].moveUp.playerMove == Static.PHONE)) {
                        path2.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                        path2.lineTo((field.field[i][j - 1].x * screenUnit).toFloat(), (field.field[i][j - 1].y * screenUnit).toFloat())
                    }
                }
                if (field.field[i][j].moveUpRight.moveDirection != null) {
                    if (field.field[i][j].moveUpRight.moveDirection!! and (field.field[i][j].moveUpRight.playerMove == Static.PHONE)) {
                        path2.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                        path2.lineTo((field.field[i + 1][j - 1].x * screenUnit).toFloat(), (field.field[i + 1][j - 1].y * screenUnit).toFloat())
                    }
                }
                if (field.field[i][j].moveRight.moveDirection != null) {
                    if (field.field[i][j].moveRight.moveDirection!! and (field.field[i][j].moveRight.playerMove == Static.PHONE)) {
                        path2.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                        path2.lineTo((field.field[i + 1][j].x * screenUnit).toFloat(), (field.field[i + 1][j].y * screenUnit).toFloat())
                    }
                }
                if (field.field[i][j].moveDownRight.moveDirection != null) {
                    if (field.field[i][j].moveDownRight.moveDirection!! and (field.field[i][j].moveDownRight.playerMove == Static.PHONE)) {
                        path2.moveTo((field.field[i][j].x * screenUnit).toFloat(), (field.field[i][j].y * screenUnit).toFloat())
                        path2.lineTo((field.field[i + 1][j + 1].x * screenUnit).toFloat(), (field.field[i + 1][j + 1].y * screenUnit).toFloat())
                    }
                }
            }
        }
        canvas.drawPath(path2,paint)

    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

}