package com.tt.oldschoolsoccer.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tt.oldschoolsoccer.R

class FieldHardDrawable(private val context: Context, private val screenUnit:Double): Drawable() {
    private val paint = Paint()
    private val lineWidth = screenUnit/10
    private val a = Point(screenUnit.toInt(), (2*screenUnit).toInt())
    private val b = Point((a.x+4*screenUnit).toInt(),a.y)
    private val c = Point((b.x),(b.y-screenUnit).toInt())
    private val d = Point((c.x+4*screenUnit).toInt(),(c.y))
    private val e = Point((d.x),(d.y+screenUnit).toInt())
    private val f = Point((e.x+4*screenUnit).toInt(),(e.y))
    private val g = Point((f.x),(f.y+18*screenUnit).toInt())
    private val h = Point((g.x-4*screenUnit).toInt(), (g.y))
    private val i = Point((h.x).toInt(), (h.y+screenUnit).toInt())
    private val j = Point((i.x-4*screenUnit).toInt(), (i.y).toInt())
    private val k = Point((j.x).toInt(), (j.y-screenUnit).toInt())
    private val l = Point((k.x-4*screenUnit).toInt(), (k.y).toInt())

    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color= ContextCompat.getColor(context, R.color.black)
        paint.strokeWidth=lineWidth.toFloat()

        val path = Path()
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.lineTo(b.x.toFloat(), b.y.toFloat())
        path.lineTo(c.x.toFloat(), c.y.toFloat())
        path.lineTo(d.x.toFloat(), d.y.toFloat())
        path.lineTo(e.x.toFloat(), e.y.toFloat())
        path.lineTo(f.x.toFloat(), f.y.toFloat())
        path.lineTo(g.x.toFloat(), g.y.toFloat())
        path.lineTo(h.x.toFloat(), h.y.toFloat())
        path.lineTo(i.x.toFloat(), i.y.toFloat())
        path.lineTo(j.x.toFloat(), j.y.toFloat())
        path.lineTo(k.x.toFloat(), k.y.toFloat())
        path.lineTo(l.x.toFloat(), l.y.toFloat())
        path.close()

        canvas.drawPath(path,paint)

        canvas.drawCircle((screenUnit*7).toFloat(), (screenUnit*11).toFloat(), (screenUnit/20).toFloat(),paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha=alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter=colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}