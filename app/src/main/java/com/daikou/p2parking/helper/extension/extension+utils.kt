package com.daikou.p2parking.helper.extension

import android.view.View

fun View.checkEnableView() {
    this.isEnabled = false
    this.postDelayed({ this.isEnabled = true }, 500)
}