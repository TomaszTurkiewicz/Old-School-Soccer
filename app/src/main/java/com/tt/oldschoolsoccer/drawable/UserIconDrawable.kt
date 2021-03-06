package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.telecom.CallScreeningService
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R

class UserIconDrawable(private val context: Context, private val size:Double, private val screenUnit:Double): Drawable() {

    private val paint = Paint()
    private val lineWidth = screenUnit/10
    private val dif = 0.5
    private val a = 4 - dif
    private val b = 5.5 - dif
    private val c = 6.5 - dif
    private val d = 6 - dif
    private val e = 8 - dif


    override fun draw(canvas: Canvas) {
        val rect = RectF(0f,0f,size.toFloat(),size.toFloat())
        paint.style = Paint.Style.STROKE
        paint.color = ContextCompat.getColor(context, R.color.win)
        paint.strokeWidth = lineWidth.toFloat()
        canvas.drawRect(rect,paint)

        paint.style = Paint.Style.FILL

        val pathLeftArm = Path()
        pathLeftArm.moveTo((a*screenUnit).toFloat(),4*screenUnit.toFloat())
        pathLeftArm.lineTo((b*screenUnit).toFloat(),(5.5*screenUnit).toFloat())
        pathLeftArm.lineTo((c*screenUnit).toFloat(),(4.5*screenUnit).toFloat())
        pathLeftArm.lineTo((c*screenUnit).toFloat(),(2.5*screenUnit).toFloat())
        pathLeftArm.lineTo((d*screenUnit).toFloat(),(2*screenUnit).toFloat())
        pathLeftArm.close()
        canvas.drawPath(pathLeftArm,paint)


        val pathRightArm = Path()
        pathRightArm.moveTo(((18-a)*screenUnit).toFloat(),4*screenUnit.toFloat())
        pathRightArm.lineTo(((18-b)*screenUnit).toFloat(),(5.5*screenUnit).toFloat())
        pathRightArm.lineTo(((18-c)*screenUnit).toFloat(),(4.5*screenUnit).toFloat())
        pathRightArm.lineTo(((18-c)*screenUnit).toFloat(),(2.5*screenUnit).toFloat())
        pathRightArm.lineTo(((18-d)*screenUnit).toFloat(),(2*screenUnit).toFloat())
        pathRightArm.close()
        canvas.drawPath(pathRightArm,paint)



        val pathLeftOverArm = Path()
        pathLeftOverArm.moveTo((a*screenUnit).toFloat(),4*screenUnit.toFloat())
        pathLeftOverArm.lineTo(((a-0.25)*screenUnit).toFloat(),(3.75*screenUnit).toFloat())
        pathLeftOverArm.lineTo(((d)*screenUnit).toFloat(),(1.5*screenUnit).toFloat())
        pathLeftOverArm.lineTo(((e)*screenUnit).toFloat(),(0.75*screenUnit).toFloat())
        pathLeftOverArm.lineTo(((e+0.25)*screenUnit).toFloat(),(1*screenUnit).toFloat())
        pathLeftOverArm.lineTo(((d+0.25)*screenUnit).toFloat(),(1.75*screenUnit).toFloat())
        pathLeftOverArm.close()

        canvas.drawPath(pathLeftOverArm,paint)



        val pathRightOverArm = Path()
        pathRightOverArm.moveTo(((18-a)*screenUnit).toFloat(),4*screenUnit.toFloat())
        pathRightOverArm.lineTo(((18-(a-0.25))*screenUnit).toFloat(),(3.75*screenUnit).toFloat())
        pathRightOverArm.lineTo(((18-d)*screenUnit).toFloat(),(1.5*screenUnit).toFloat())
        pathRightOverArm.lineTo(((18-e)*screenUnit).toFloat(),(0.75*screenUnit).toFloat())
        pathRightOverArm.lineTo(((18-(e+0.25))*screenUnit).toFloat(),(1*screenUnit).toFloat())
        pathRightOverArm.lineTo(((18-(d+0.25))*screenUnit).toFloat(),(1.75*screenUnit).toFloat())


        pathRightOverArm.close()
        canvas.drawPath(pathRightOverArm,paint)


        val pathBodyLeft = Path()

        pathBodyLeft.moveTo((c*screenUnit).toFloat(),(2.5*screenUnit).toFloat())
        pathBodyLeft.lineTo((d*screenUnit).toFloat(),(2*screenUnit).toFloat())
        pathBodyLeft.lineTo(((d+0.25)*screenUnit).toFloat(),(1.75*screenUnit).toFloat())
        pathBodyLeft.lineTo(((e+0.25)*screenUnit).toFloat(),(1*screenUnit).toFloat())
        pathBodyLeft.lineTo((9*screenUnit).toFloat(),(2.25*screenUnit).toFloat())
        pathBodyLeft.lineTo((9*screenUnit).toFloat(),(10.5*screenUnit).toFloat())
        pathBodyLeft.lineTo((c*screenUnit).toFloat(),(10.5*screenUnit).toFloat())

        pathBodyLeft.close()
        canvas.drawPath(pathBodyLeft,paint)



        val pathBodyRight = Path()

        pathBodyRight.moveTo(((18-c)*screenUnit).toFloat(),(2.5*screenUnit).toFloat())
        pathBodyRight.lineTo(((18-d)*screenUnit).toFloat(),(2*screenUnit).toFloat())
        pathBodyRight.lineTo(((18-(d+0.25))*screenUnit).toFloat(),(1.75*screenUnit).toFloat())
        pathBodyRight.lineTo(((18-(e+0.25))*screenUnit).toFloat(),(1*screenUnit).toFloat())
        pathBodyRight.lineTo((9*screenUnit).toFloat(),(2.25*screenUnit).toFloat())
        pathBodyRight.lineTo((9*screenUnit).toFloat(),(10.5*screenUnit).toFloat())
        pathBodyRight.lineTo(((18-c)*screenUnit).toFloat(),(10.5*screenUnit).toFloat())

        pathBodyRight.close()
        canvas.drawPath(pathBodyRight,paint)


        val pathLeftTrousersExternal = Path()

        pathLeftTrousersExternal.moveTo((c*screenUnit).toFloat(),(10.5*screenUnit).toFloat())
        pathLeftTrousersExternal.lineTo((b*screenUnit).toFloat(),(17*screenUnit).toFloat())
        pathLeftTrousersExternal.lineTo(((b+0.5)*screenUnit).toFloat(),(17.1*screenUnit).toFloat())
        pathLeftTrousersExternal.lineTo(((c+0.5)*screenUnit).toFloat(),(10.5*screenUnit).toFloat())
        pathLeftTrousersExternal.close()
        canvas.drawPath(pathLeftTrousersExternal,paint)



        val pathRightTrousersExternal = Path()

        pathRightTrousersExternal.moveTo(((18-c)*screenUnit).toFloat(),(10.5*screenUnit).toFloat())
        pathRightTrousersExternal.lineTo(((18-b)*screenUnit).toFloat(),(17*screenUnit).toFloat())
        pathRightTrousersExternal.lineTo(((18-(b+0.5))*screenUnit).toFloat(),(17.1*screenUnit).toFloat())
        pathRightTrousersExternal.lineTo(((18-(c+0.5))*screenUnit).toFloat(),(10.5*screenUnit).toFloat())
        pathRightTrousersExternal.close()
        canvas.drawPath(pathRightTrousersExternal,paint)



        val pathLeftTrousersInternal = Path()

        pathLeftTrousersInternal.moveTo((8*screenUnit).toFloat(), (17.43*screenUnit).toFloat())
        pathLeftTrousersInternal.lineTo((8.5*screenUnit).toFloat(), (17.5*screenUnit).toFloat())
        pathLeftTrousersInternal.lineTo((9*screenUnit).toFloat(), (14*screenUnit).toFloat())
        pathLeftTrousersInternal.lineTo((8.5*screenUnit).toFloat(), (13.93*screenUnit).toFloat())
        pathLeftTrousersInternal.close()

        canvas.drawPath(pathLeftTrousersInternal,paint)


        val pathRightTrousersInternal = Path()

        pathRightTrousersInternal.moveTo((10*screenUnit).toFloat(), (17.43*screenUnit).toFloat())
        pathRightTrousersInternal.lineTo((9.5*screenUnit).toFloat(), (17.5*screenUnit).toFloat())
        pathRightTrousersInternal.lineTo((9*screenUnit).toFloat(), (14*screenUnit).toFloat())
        pathRightTrousersInternal.lineTo((9.5*screenUnit).toFloat(), (13.93*screenUnit).toFloat())
        pathRightTrousersInternal.close()

        canvas.drawPath(pathRightTrousersInternal,paint)

        val pathTrousersInternalMiddle = Path()
        pathTrousersInternalMiddle.moveTo((8.5*screenUnit).toFloat(), (13.93*screenUnit).toFloat())
        pathTrousersInternalMiddle.lineTo((9*screenUnit).toFloat(), (14*screenUnit).toFloat())
        pathTrousersInternalMiddle.lineTo((9.5*screenUnit).toFloat(), (13.93*screenUnit).toFloat())
        pathTrousersInternalMiddle.close()

        canvas.drawPath(pathTrousersInternalMiddle,paint)


        val pathTrousersBody = Path()

        pathTrousersBody.moveTo(((c+0.5)*screenUnit).toFloat(),(10.5*screenUnit).toFloat())
        pathTrousersBody.lineTo(((b+0.5)*screenUnit).toFloat(),(17.1*screenUnit).toFloat())
        pathTrousersBody.lineTo((8*screenUnit).toFloat(), (17.43*screenUnit).toFloat())
        pathTrousersBody.lineTo((8.5*screenUnit).toFloat(), (13.93*screenUnit).toFloat())
        pathTrousersBody.lineTo((9.5*screenUnit).toFloat(), (13.93*screenUnit).toFloat())
        pathTrousersBody.lineTo((10*screenUnit).toFloat(), (17.43*screenUnit).toFloat())
        pathTrousersBody.lineTo(((18-(b+0.5))*screenUnit).toFloat(),(17.1*screenUnit).toFloat())
        pathTrousersBody.lineTo(((18-(c+0.5))*screenUnit).toFloat(),(10.5*screenUnit).toFloat())
        pathTrousersBody.close()

        canvas.drawPath(pathTrousersBody,paint)






    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}