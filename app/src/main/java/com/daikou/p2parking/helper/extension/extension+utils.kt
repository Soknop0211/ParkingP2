package com.daikou.p2parking.helper.extension

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat

fun View.checkEnableView() {
    this.isEnabled = false
    this.postDelayed({ this.isEnabled = true }, 500)
}

fun View.setBackgroundTint(color : Int) {
    this.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}

