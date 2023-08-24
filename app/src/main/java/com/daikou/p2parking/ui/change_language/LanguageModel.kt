package com.daikou.p2parking.ui.change_language

import android.content.Context
import com.daikou.p2parking.R

class LanguageModel(key: String?, var nameLanguage: String, var iconFlag: Int) {
    var key: String? = key
        private set

    companion object {
        @JvmStatic
        fun getLanguage(context: Context): ArrayList<LanguageModel> {
            val list = ArrayList<LanguageModel>()
            list.add(
                LanguageModel(
                    "en",
                    context.resources.getString(R.string.english),
                    R.drawable.united_kingdom_flag
                )
            )
            list.add(
                LanguageModel(
                    "kh",
                    context.resources.getString(R.string.khmer),
                    R.drawable.cambodia_flag
                )
            )
            return list
        }
    }
}