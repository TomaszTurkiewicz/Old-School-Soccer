package com.tt.oldschoolsoccer.classes

class UserIconColors() {
    private var backgroundColor = 0
    private var leftArmColor = 0
    private var rightArmColor = 0
    private var overArmsColor = 0
    private var bodyLeftColor = 0
    private var bodyRightColor = 0
    private var trousersExternalColor = 0
    private var trousersInternalColor = 0
    private var trousersBodyColor = 0


    fun getTrousersBodyColor():Int{
        return this.trousersBodyColor
    }

    fun getTrousersInternalColor():Int{
        return this.trousersInternalColor
    }

    fun getTrousersExternalColor():Int{
        return this.trousersExternalColor
    }

    fun getBodyRightColor():Int{
        return this.bodyRightColor
    }

    fun getBodyLeftColor():Int{
        return this.bodyLeftColor
    }

    fun getOverArmsColor():Int{
        return this.overArmsColor
    }

    fun getRightArmColor():Int{
        return this.rightArmColor
    }

    fun getLeftArmColor():Int{
        return this.leftArmColor
    }

    fun getBackgroundColor():Int{
        return this.backgroundColor
    }

    fun setTrousersBodyColor(color:Int){
        this.trousersBodyColor = color
    }

    fun setTrousersInternalColor(color:Int){
        this.trousersInternalColor = color
    }

    fun setTrousersExternalColor(color:Int){
        this.trousersExternalColor = color
    }

    fun setBodyRightColor(color:Int){
        this.bodyRightColor = color
    }

    fun setBodyLeftColor(color:Int){
        this.bodyLeftColor = color
    }

    fun setOverArmColor(color:Int){
        this.overArmsColor = color
    }

    fun setRightArmColor(color:Int){
        this.rightArmColor = color
    }

    fun setLeftArmColor(color:Int){
        this.leftArmColor = color
    }

    fun setBackgroundColor(color:Int){
        this.backgroundColor = color
    }


    fun notLoggedIn(){
        this.backgroundColor = -1
        this.leftArmColor = -1
        this.rightArmColor = -1
        this.overArmsColor = -1
        this.bodyLeftColor = -1
        this.bodyRightColor = -1
        this.trousersExternalColor = -1
        this.trousersInternalColor = -1
        this.trousersBodyColor = -1
    }
}

