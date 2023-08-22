package com.daikou.p2parking.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.daikou.p2parking.base.ApiResWraper
import com.daikou.p2parking.base.base.BaseViewModel
import com.daikou.p2parking.data.call_back.IApiResWrapper
import com.daikou.p2parking.data.repository.Repository
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.collections.HashMap

class LoginViewModel
@Inject constructor(
    private val context: Context,
    override val repository: Repository
) :
    BaseViewModel(context, repository) {

    private val _loadingLoginLiveData = MutableLiveData<Boolean>()
    private val _dataLoginLiveData = MutableLiveData<ApiResWraper<JsonElement>>()

    val loadingLoginLiveData: MutableLiveData<Boolean> get() = _loadingLoginLiveData
    val dataLoginLiveData: MutableLiveData<ApiResWraper<JsonElement>> get() = _dataLoginLiveData

    fun login(bodyMap: HashMap<String, Any>) {
        val requestFlow: Flow<ApiResWraper<JsonElement>> = repository.login(bodyMap)
        submit(requestFlow, object : IApiResWrapper<ApiResWraper<JsonElement>> {
            override fun onLoading(hasLoading: Boolean) {
                loadingLoginLiveData.value = hasLoading
            }

            override fun onData(respondData: ApiResWraper<JsonElement>) {
                if (respondData.success) {
                    dataLoginLiveData.value = respondData
                }
            }

            override fun onError(
                message: String,
                code: Int,
                errorHashMap: JsonObject,
            ) {
                dataLoginLiveData.value = ApiResWraper(
                    code,
                    message,
                    false,
                    errorHashMap,
                )
            }

        })
    }

}