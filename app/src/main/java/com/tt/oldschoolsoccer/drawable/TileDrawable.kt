package com.tt.oldschoolsoccer.drawable

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore.Images.Media.getBitmap

class TileDrawable(drawable: Drawable,tileMode: Shader.TileMode,x:Int):Drawable() {

    private val paint=Paint()

    init {
        paint.shader = BitmapShader(getBitmap(drawable,x),tileMode,tileMode)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPaint(paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return  PixelFormat.TRANSLUCENT
    }

    private fun getBitmap(drawable:Drawable, x:Int):Bitmap{
        if(drawable is BitmapDrawable)
            return (drawable).bitmap
        val bitmap:Bitmap = Bitmap.createBitmap(x,x,Bitmap.Config.ARGB_8888)
        val canvas:Canvas = Canvas(bitmap)
        drawable.setBounds(0,0,x,x)
        drawable.draw(canvas)
        return bitmap
    }
}