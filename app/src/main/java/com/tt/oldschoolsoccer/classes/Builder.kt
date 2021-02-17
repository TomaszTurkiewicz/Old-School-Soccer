package com.tt.oldschoolsoccer.classes

import android.content.Context

class Builder(private val context: Context) {
    private lateinit var onUpdateNeededListener1: UpdateHelper.OnUpdateNeededListener

    fun onUpdateNeeded(onUpdateNeededListener: UpdateHelper.OnUpdateNeededListener):Builder{
        onUpdateNeededListener1 = onUpdateNeededListener
        return this
    }

    fun build():UpdateHelper{
        return UpdateHelper(context,onUpdateNeededListener1)
    }

    fun check():UpdateHelper{
        val updateHelper:UpdateHelper = build()
        updateHelper.check()
        return updateHelper
    }
}