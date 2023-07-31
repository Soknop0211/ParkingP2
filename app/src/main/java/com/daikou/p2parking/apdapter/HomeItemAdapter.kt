package com.daikou.p2parking.apdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daikou.p2parking.data.model.HomeItemModel
import com.daikou.p2parking.databinding.HomeItemLayoutBinding

class HomeItemAdapter: RecyclerView.Adapter<HomeItemAdapter.ViewHolder>(){
    private lateinit var HomeItemList : ArrayList<HomeItemModel>
    lateinit var selectedItem: (homeItem : HomeItemModel)-> Unit

    fun setRow(itemList : ArrayList<HomeItemModel>){
        this.HomeItemList = itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutBinding = HomeItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(layoutBinding)
    }

    override fun getItemCount(): Int {
       return HomeItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val homeItemModel = HomeItemList[position]
        holder.bind(homeItemModel)
        holder.homeItemLayoutBinding.root.setOnClickListener {
            selectedItem.invoke(homeItemModel)
        }
    }

    class ViewHolder(val homeItemLayoutBinding: HomeItemLayoutBinding) : RecyclerView.ViewHolder(homeItemLayoutBinding.root){
        fun bind(homeItemModel: HomeItemModel){
            homeItemLayoutBinding.imageLogo.setImageResource(homeItemModel.imageLogo?: -1)
            homeItemLayoutBinding.title.text = homeItemModel.title ?: "N/A"
        }
    }
}