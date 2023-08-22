package com.daikou.p2parking.base

import android.content.Intent
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

open class BaseCoreActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val activityLauncher : BetterActivityResult<Intent, ActivityResult> = BetterActivityResult.registerActivityForResult(this)

    fun <T : BaseCoreActivity?> self(): T {
        return this as T
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

}