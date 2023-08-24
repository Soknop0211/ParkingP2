package com.daikou.p2parking.ui.change_language

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daikou.p2parking.R
import com.daikou.p2parking.helper.HelperUtil.getStringSharePreference
import com.daikou.p2parking.model.Constants
import com.daikou.p2parking.ui.change_language.ChangeLangAdapter.ItemViewHolder

class ChangeLangAdapter(
    private val context: Context,
    private val modelList: List<LanguageModel>,
    private val clickCallBackListener: ClickCallBackListener
) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val rowView =
            LayoutInflater.from(parent.context).inflate(R.layout.language_model, parent, false)
        return ItemViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val languageModel : LanguageModel = modelList[position]
        if (languageModel != null) {
            holder.iconFlag.setImageResource(languageModel.iconFlag)
            holder.nameLanguage.text = languageModel.nameLanguage
            val getLangSaved = getStringSharePreference(context, Constants.Auth.LANGUAGE)
            if (getLangSaved.equals(languageModel.key, ignoreCase = true)) {
                holder.iconTech.visibility = View.VISIBLE
            } else {
                holder.iconTech.visibility = View.INVISIBLE
            }
            holder.nameLanguage.setTextColor(ContextCompat.getColor(context, R.color.black))
            holder.itemView.setOnClickListener {
                clickCallBackListener.ClickCallBack(
                    position,
                    languageModel.key,
                    context
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameLanguage: TextView
        val iconFlag: ImageView
        val iconTech: ImageView

        init {
            nameLanguage = itemView.findViewById(R.id.name_language)
            iconFlag = itemView.findViewById(R.id.icon_flag)
            iconTech = itemView.findViewById(R.id.icon_tech_default)
        }
    }

    interface ClickCallBackListener {
        fun ClickCallBack(pos: Int, lang: String?, context: Context?)
    }
}