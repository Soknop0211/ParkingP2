package com.daikou.p2parking.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.daikou.p2parking.R
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.Config
import com.daikou.p2parking.databinding.ActivityLotTypeBinding
import com.daikou.p2parking.model.LotTypeModel
import com.daikou.p2parking.ui.adapter.LotTypeAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class LotTypeActivity : BaseActivity() {

    private lateinit var binding : ActivityLotTypeBinding
    private var mList = ArrayList<LotTypeModel>()

    companion object {
        const val LotTypeResponse = "LotTypeResponse"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLotTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = resources.getString(R.string.lot_type)

        // Init View
        binding.toolbar.title.text = resources.getString(R.string.log_in)
        binding.toolbar.iconBack.visibility = View.VISIBLE
        binding.toolbar.iconBack.setOnClickListener { finish() }

        if (intent != null && intent.hasExtra(LotTypeResponse)) {
            val jsonDataListBank = intent.getStringExtra(LotTypeResponse)
            val gson = Gson()
            val bankAccountTypeToken =
                object : TypeToken<ArrayList<LotTypeModel>>() {}.type
            mList = gson.fromJson(jsonDataListBank, bankAccountTypeToken)
        }

        val mLotTypeAdapter = LotTypeAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(self())
            adapter = mLotTypeAdapter
        }
        mLotTypeAdapter.addHomeScreen(mList)

        mLotTypeAdapter.selectedRow = {
            val intent = Intent()
            intent.putExtra(LotTypeResponse, Config.GsonConverterHelper.convertGenericClassToJson(it))
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}