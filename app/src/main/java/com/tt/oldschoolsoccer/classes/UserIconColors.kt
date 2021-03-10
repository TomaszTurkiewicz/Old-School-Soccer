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

    fun addOneToBackgroundColor() {
        if (backgroundColor == Static.BACKGROUND_COLORS_MAX_VALUE) {
            this.backgroundColor = 0
        } else {
            this.backgroundColor += 1
        }
    }

    fun minusOneFromBackgroundColor(){
        if(backgroundColor == 0){
            this.backgroundColor = Static.BACKGROUND_COLORS_MAX_VALUE
        }else{
            this.backgroundColor -= 1
        }
    }

    fun addOneToOverArmsColor() {
        if (overArmsColor == Static.OTHER_COLORS_MAX_VALUE) {
            this.overArmsColor = 0
        } else {
            this.overArmsColor += 1
        }
    }

    fun minusOneFromArmsColor(){
        if(overArmsColor == 0){
            this.overArmsColor = Static.OTHER_COLORS_MAX_VALUE
        }else{
            this.overArmsColor -= 1
        }
    }

    fun addOneToLeftSleeveColor() {
        if (leftArmColor == Static.OTHER_COLORS_MAX_VALUE) {
            this.leftArmColor = 0
        } else {
            this.leftArmColor += 1
        }
    }

    fun minusOneFromLeftSleeveColor(){
        if(leftArmColor == 0){
            this.leftArmColor = Static.OTHER_COLORS_MAX_VALUE
        }else{
            this.leftArmColor -= 1
        }
    }

    fun addOneToRightSleeveColor() {
        if (rightArmColor == Static.OTHER_COLORS_MAX_VALUE) {
            this.rightArmColor = 0
        } else {
            this.rightArmColor += 1
        }
    }

    fun minusOneFromRightSleeveColor(){
        if(rightArmColor == 0){
            this.rightArmColor = Static.OTHER_COLORS_MAX_VALUE
        }else{
            this.rightArmColor -= 1
        }
    }

    fun addOneToLeftShirtColor() {
        if (bodyLeftColor == Static.OTHER_COLORS_MAX_VALUE) {
            this.bodyLeftColor = 0
        } else {
            this.bodyLeftColor += 1
        }
    }

    fun minusOneFromLeftShirtColor(){
        if(bodyLeftColor == 0){
            this.bodyLeftColor = Static.OTHER_COLORS_MAX_VALUE
        }else{
            this.bodyLeftColor -= 1
        }
    }

    fun addOneToRightShirtColor() {
        if (bodyRightColor == Static.OTHER_COLORS_MAX_VALUE) {
            this.bodyRightColor = 0
        } else {
            this.bodyRightColor += 1
        }
    }

    fun minusOneFromRightShirtColor(){
        if(bodyRightColor == 0){
            this.bodyRightColor = Static.OTHER_COLORS_MAX_VALUE
        }else{
            this.bodyRightColor -= 1
        }
    }

    fun addOneToTrouserColor() {
        if (trousersBodyColor == Static.OTHER_COLORS_MAX_VALUE) {
            this.trousersBodyColor = 0
        } else {
            this.trousersBodyColor += 1
        }
    }

    fun minusOneFromTrouserColor(){
        if(trousersBodyColor == 0){
            this.trousersBodyColor = Static.OTHER_COLORS_MAX_VALUE
        }else{
            this.trousersBodyColor -= 1
        }
    }

    fun addOneToTrouserExternalColor() {
        if (trousersExternalColor == Static.OTHER_COLORS_MAX_VALUE) {
            this.trousersExternalColor = 0
        } else {
            this.trousersExternalColor += 1
        }
    }

    fun minusOneFromTrouserExternalColor(){
        if(trousersExternalColor == 0){
            this.trousersExternalColor = Static.OTHER_COLORS_MAX_VALUE
        }else{
            this.trousersExternalColor -= 1
        }
    }

    fun addOneToTrouserInternalColor() {
        if (trousersInternalColor == Static.OTHER_COLORS_MAX_VALUE) {
            this.trousersInternalColor = 0
        } else {
            this.trousersInternalColor += 1
        }
    }

    fun minusOneFromTrouserInternalColor(){
        if(trousersInternalColor == 0){
            this.trousersInternalColor = Static.OTHER_COLORS_MAX_VALUE
        }else{
            this.trousersInternalColor -= 1
        }
    }

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


    fun notLoggedIn():UserIconColors{
        this.backgroundColor = -1
        this.leftArmColor = -1
        this.rightArmColor = -1
        this.overArmsColor = -1
        this.bodyLeftColor = -1
        this.bodyRightColor = -1
        this.trousersExternalColor = -1
        this.trousersInternalColor = -1
        this.trousersBodyColor = -1
        return this
    }
}

