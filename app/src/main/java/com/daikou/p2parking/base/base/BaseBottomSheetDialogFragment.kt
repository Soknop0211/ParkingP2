package com.daikou.p2parking.base.base

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.daikou.p2parking.helper.MessageUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    var mContextBase: Context? = null
    var fContextBase: FragmentActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContextBase = context
        fContextBase = context as FragmentActivity
    }

    protected fun <T : BaseBottomSheetDialogFragment?> self(): T {
        return this as T
    }

    fun globalShowError(text: String) {
        MessageUtils.showError(
            self(),
            "",
            "$text"
        )
    }

}