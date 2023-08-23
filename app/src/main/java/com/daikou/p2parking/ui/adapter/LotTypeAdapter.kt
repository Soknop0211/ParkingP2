package com.daikou.p2parking.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daikou.p2parking.R
import com.daikou.p2parking.model.LotTypeModel

@SuppressLint("NotifyDataSetChanged")
class LotTypeAdapter : RecyclerView.Adapter<LotTypeAdapter.HomeScreenHolder>() {
    private var homeScreenModel = ArrayList<LotTypeModel>()
    var selectedRow: ((LotTypeModel) -> Unit?)? = null

    fun addHomeScreen(homeScreenModel: ArrayList<LotTypeModel>) {
        this.homeScreenModel.clear()
        this.homeScreenModel = homeScreenModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeScreenHolder {
        return HomeScreenHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_view_text_view_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeScreenHolder, position: Int) {
        val model = homeScreenModel[position]

        holder.bind(model)

        holder.itemView.setOnClickListener {

            it.isEnabled = false
            it.postDelayed({ it.isEnabled = true }, 500)

            selectedRow?.invoke(model)
        }
    }

    override fun getItemCount(): Int {
        return homeScreenModel.size
    }

    class HomeScreenHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemName: TextView = itemView.findViewById(R.id.item_name)

        fun bind(homeScreenModel: LotTypeModel) {
            itemName.text = if (homeScreenModel.name != null)   homeScreenModel.name else ". . ."
        }

    }

}