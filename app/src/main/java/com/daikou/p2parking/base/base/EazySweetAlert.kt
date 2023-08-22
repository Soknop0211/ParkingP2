package com.daikou.p2parking.base.base

import android.content.Context
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog

class EazySweetAlert(context: Context?, alertType: Int) :
    SweetAlertDialog(context, alertType) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



}