package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R

class UserIconDrawable(private val context: Context, size:Double): Drawable() {

    private val paint = Paint()

    private val unit = size/18
    private val lineWidth = unit/10
    private val dif = 0.5
    private val a = 4 - dif
    private val b = 5.5 - dif
    private val c = 6.5 - dif
    private val d = 6 - dif
    private val e = 8 - dif
    private val radius = size/2


    override fun draw(canvas: Canvas) {

        /**
         * first variable background
         */

        paint.color = ContextCompat.getColor(context,R.color.test)
        paint.style = Paint.Style.FILL

        canvas.drawCircle((9*unit).toFloat(), (9*unit).toFloat(), radius.toFloat(),paint)


        paint.color = ContextCompat.getColor(context, R.color.win)
        paint.strokeWidth = lineWidth.toFloat()
        paint.style = Paint.Style.FILL

        /**
         * second variable left arm
         */

        val pathLeftArm = Path()
        pathLeftArm.moveTo((a*unit).toFloat(),4*unit.toFloat())
        pathLeftArm.lineTo((b*unit).toFloat(),(5.5*unit).toFloat())
        pathLeftArm.lineTo((c*unit).toFloat(),(4.5*unit).toFloat())
        pathLeftArm.lineTo((c*unit).toFloat(),(2.5*unit).toFloat())
        pathLeftArm.lineTo((d*unit).toFloat(),(2*unit).toFloat())
        pathLeftArm.close()
        canvas.drawPath(pathLeftArm,paint)

        /**
         * third variable right arm
         */

        val pathRightArm = Path()
        pathRightArm.moveTo(((18-a)*unit).toFloat(),4*unit.toFloat())
        pathRightArm.lineTo(((18-b)*unit).toFloat(),(5.5*unit).toFloat())
        pathRightArm.lineTo(((18-c)*unit).toFloat(),(4.5*unit).toFloat())
        pathRightArm.lineTo(((18-c)*unit).toFloat(),(2.5*unit).toFloat())
        pathRightArm.lineTo(((18-d)*unit).toFloat(),(2*unit).toFloat())
        pathRightArm.close()
        canvas.drawPath(pathRightArm,paint)


        /**
         * fourth variable overarms
         */

        val pathLeftOverArm = Path()
        pathLeftOverArm.moveTo((a*unit).toFloat(),4*unit.toFloat())
        pathLeftOverArm.lineTo(((a-0.25)*unit).toFloat(),(3.75*unit).toFloat())
        pathLeftOverArm.lineTo(((d)*unit).toFloat(),(1.5*unit).toFloat())
        pathLeftOverArm.lineTo(((e)*unit).toFloat(),(0.75*unit).toFloat())
        pathLeftOverArm.lineTo(((e+0.25)*unit).toFloat(),(1*unit).toFloat())
        pathLeftOverArm.lineTo(((d+0.25)*unit).toFloat(),(1.75*unit).toFloat())
        pathLeftOverArm.close()

        canvas.drawPath(pathLeftOverArm,paint)



        val pathRightOverArm = Path()
        pathRightOverArm.moveTo(((18-a)*unit).toFloat(),4*unit.toFloat())
        pathRightOverArm.lineTo(((18-(a-0.25))*unit).toFloat(),(3.75*unit).toFloat())
        pathRightOverArm.lineTo(((18-d)*unit).toFloat(),(1.5*unit).toFloat())
        pathRightOverArm.lineTo(((18-e)*unit).toFloat(),(0.75*unit).toFloat())
        pathRightOverArm.lineTo(((18-(e+0.25))*unit).toFloat(),(1*unit).toFloat())
        pathRightOverArm.lineTo(((18-(d+0.25))*unit).toFloat(),(1.75*unit).toFloat())


        pathRightOverArm.close()
        canvas.drawPath(pathRightOverArm,paint)

        /**
         * fifth left body
         */

        val pathBodyLeft = Path()

        pathBodyLeft.moveTo((c*unit).toFloat(),(2.5*unit).toFloat())
        pathBodyLeft.lineTo((d*unit).toFloat(),(2*unit).toFloat())
        pathBodyLeft.lineTo(((d+0.25)*unit).toFloat(),(1.75*unit).toFloat())
        pathBodyLeft.lineTo(((e+0.25)*unit).toFloat(),(1*unit).toFloat())
        pathBodyLeft.lineTo((9*unit).toFloat(),(2.25*unit).toFloat())
        pathBodyLeft.lineTo((9*unit).toFloat(),(10.5*unit).toFloat())
        pathBodyLeft.lineTo((c*unit).toFloat(),(10.5*unit).toFloat())

        pathBodyLeft.close()
        canvas.drawPath(pathBodyLeft,paint)


        /**
         * sixth right body
         */

        val pathBodyRight = Path()

        pathBodyRight.moveTo(((18-c)*unit).toFloat(),(2.5*unit).toFloat())
        pathBodyRight.lineTo(((18-d)*unit).toFloat(),(2*unit).toFloat())
        pathBodyRight.lineTo(((18-(d+0.25))*unit).toFloat(),(1.75*unit).toFloat())
        pathBodyRight.lineTo(((18-(e+0.25))*unit).toFloat(),(1*unit).toFloat())
        pathBodyRight.lineTo((9*unit).toFloat(),(2.25*unit).toFloat())
        pathBodyRight.lineTo((9*unit).toFloat(),(10.5*unit).toFloat())
        pathBodyRight.lineTo(((18-c)*unit).toFloat(),(10.5*unit).toFloat())

        pathBodyRight.close()
        canvas.drawPath(pathBodyRight,paint)

        /**
         * seventh trousers external
         */

        val pathLeftTrousersExternal = Path()

        pathLeftTrousersExternal.moveTo((c*unit).toFloat(),(10.5*unit).toFloat())
        pathLeftTrousersExternal.lineTo((b*unit).toFloat(),(17*unit).toFloat())
        pathLeftTrousersExternal.lineTo(((b+0.5)*unit).toFloat(),(17.1*unit).toFloat())
        pathLeftTrousersExternal.lineTo(((c+0.5)*unit).toFloat(),(10.5*unit).toFloat())
        pathLeftTrousersExternal.close()
        canvas.drawPath(pathLeftTrousersExternal,paint)



        val pathRightTrousersExternal = Path()

        pathRightTrousersExternal.moveTo(((18-c)*unit).toFloat(),(10.5*unit).toFloat())
        pathRightTrousersExternal.lineTo(((18-b)*unit).toFloat(),(17*unit).toFloat())
        pathRightTrousersExternal.lineTo(((18-(b+0.5))*unit).toFloat(),(17.1*unit).toFloat())
        pathRightTrousersExternal.lineTo(((18-(c+0.5))*unit).toFloat(),(10.5*unit).toFloat())
        pathRightTrousersExternal.close()
        canvas.drawPath(pathRightTrousersExternal,paint)


        /**
         * eighth trousers internal
         */

        val pathLeftTrousersInternal = Path()

        pathLeftTrousersInternal.moveTo((8*unit).toFloat(), (17.43*unit).toFloat())
        pathLeftTrousersInternal.lineTo((8.5*unit).toFloat(), (17.5*unit).toFloat())
        pathLeftTrousersInternal.lineTo((9*unit).toFloat(), (14*unit).toFloat())
        pathLeftTrousersInternal.lineTo((8.5*unit).toFloat(), (13.93*unit).toFloat())
        pathLeftTrousersInternal.close()

        canvas.drawPath(pathLeftTrousersInternal,paint)


        val pathRightTrousersInternal = Path()

        pathRightTrousersInternal.moveTo((10*unit).toFloat(), (17.43*unit).toFloat())
        pathRightTrousersInternal.lineTo((9.5*unit).toFloat(), (17.5*unit).toFloat())
        pathRightTrousersInternal.lineTo((9*unit).toFloat(), (14*unit).toFloat())
        pathRightTrousersInternal.lineTo((9.5*unit).toFloat(), (13.93*unit).toFloat())
        pathRightTrousersInternal.close()

        canvas.drawPath(pathRightTrousersInternal,paint)

        val pathTrousersInternalMiddle = Path()
        pathTrousersInternalMiddle.moveTo((8.5*unit).toFloat(), (13.93*unit).toFloat())
        pathTrousersInternalMiddle.lineTo((9*unit).toFloat(), (14*unit).toFloat())
        pathTrousersInternalMiddle.lineTo((9.5*unit).toFloat(), (13.93*unit).toFloat())
        pathTrousersInternalMiddle.close()

        canvas.drawPath(pathTrousersInternalMiddle,paint)


        /**
         * ninth trousers body
         */

        val pathTrousersBody = Path()

        pathTrousersBody.moveTo(((c+0.5)*unit).toFloat(),(10.5*unit).toFloat())
        pathTrousersBody.lineTo(((b+0.5)*unit).toFloat(),(17.1*unit).toFloat())
        pathTrousersBody.lineTo((8*unit).toFloat(), (17.43*unit).toFloat())
        pathTrousersBody.lineTo((8.5*unit).toFloat(), (13.93*unit).toFloat())
        pathTrousersBody.lineTo((9.5*unit).toFloat(), (13.93*unit).toFloat())
        pathTrousersBody.lineTo((10*unit).toFloat(), (17.43*unit).toFloat())
        pathTrousersBody.lineTo(((18-(b+0.5))*unit).toFloat(),(17.1*unit).toFloat())
        pathTrousersBody.lineTo(((18-(c+0.5))*unit).toFloat(),(10.5*unit).toFloat())
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