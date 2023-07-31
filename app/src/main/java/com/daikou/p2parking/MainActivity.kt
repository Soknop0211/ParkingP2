package com.daikou.p2parking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.daikou.p2parking.apdapter.HomeItemAdapter
import com.daikou.p2parking.data.model.HomeItemModel
import com.daikou.p2parking.databinding.ActivityMainBinding
import com.daikou.p2parking.emunUtil.HomeScreenEnum
import com.daikou.p2parking.ui.car_photo_taking.CheckInActivity
import com.daikou.p2parking.ui.scan_check_out.ScanActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var homeItemAdapter: HomeItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupHomeItem()
    }

    private fun setupHomeItem() {
        val itemList  = ArrayList<HomeItemModel>()
        itemList.add(HomeItemModel(R.drawable.car,"Check In", HomeScreenEnum.TakePhoto))
        itemList.add(HomeItemModel(R.drawable.scan,"Check Out", HomeScreenEnum.ScanQR))
        homeItemAdapter = HomeItemAdapter()
        homeItemAdapter.setRow(itemList)
        binding.recyclerViewHome.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewHome.adapter = homeItemAdapter
        homeItemAdapter.selectedItem = { homeItem ->
            when (homeItem.action ){
                HomeScreenEnum.TakePhoto -> {
                    val intent = Intent(this@MainActivity, CheckInActivity::class.java)
                    startActivity(intent)
                }
                HomeScreenEnum.ScanQR ->{
                    val intent = Intent(this@MainActivity, ScanActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}