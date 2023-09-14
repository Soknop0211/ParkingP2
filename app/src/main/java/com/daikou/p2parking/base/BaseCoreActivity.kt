package com.daikou.p2parking.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.daikou.p2parking.helper.HelperUtil
import com.daikou.p2parking.model.Constants
import com.daikou.p2parking.model.User
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

open class BaseCoreActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val activityLauncher : BetterActivityResult<Intent, ActivityResult> = BetterActivityResult.registerActivityForResult(this)
    var timerHandler: Handler? = null

    fun <T : BaseCoreActivity?> self(): T {
        return this as T
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        timerHandler = Handler(Looper.getMainLooper())
    }

    fun convertAsBitmap(
        imageBitmap: ImageView,
        image: String,
    ) {
        Glide
            .with(this)
            .asBitmap()
            .load(image)
            .into(imageBitmap)
    }
    fun hasInternetConnection(): Boolean {
        return !HelperUtil.hasNotNetworkAvailable(self())
    }
}