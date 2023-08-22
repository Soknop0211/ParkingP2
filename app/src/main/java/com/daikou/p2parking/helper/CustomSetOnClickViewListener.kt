package com.daikou.p2parking.helper

import android.view.View
import com.daikou.p2parking.helper.extension.checkEnableView

class CustomSetOnClickViewListener
    (private val customResponseOnClickListener: CustomResponseOnClickListener) : View.OnClickListener {

    override fun onClick(v: View) {
        v.checkEnableView()
        customResponseOnClickListener.onClick(v)
    }
}