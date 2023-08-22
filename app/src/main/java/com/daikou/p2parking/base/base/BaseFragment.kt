package com.daikou.p2parking.base.base

import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

open class BaseFragment : DaggerFragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    protected fun <T : BaseFragment?> self(): T {
        return this as T
    }
}