package com.daikou.p2parking.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.daikou.p2parking.base.ApiResWraper
import com.daikou.p2parking.base.base.BaseViewModel
import com.daikou.p2parking.data.call_back.IApiResWrapper
import com.daikou.p2parking.data.model.TicketModel
import com.daikou.p2parking.data.repository.Repository
import com.daikou.p2parking.model.LotTypeModel
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LotTypeViewModel @Inject constructor(
    private val context: Context,
    override val repository: Repository
) :
    BaseViewModel(context, repository) {

    /**** Lot type *****/

    private val _loadingLoginLiveData = MutableLiveData<Boolean>()
    private val _dataListAllLotLiveData =
        MutableLiveData<ApiResWraper<List<LotTypeModel>>>()

    val loadingLoginLiveData: MutableLiveData<Boolean> get() = _loadingLoginLiveData
    val dataListAllLotLiveDataLiveData: MutableLiveData<ApiResWraper<List<LotTypeModel>>> get() = _dataListAllLotLiveData

    fun fetchLotType() {
        val requestFlow: Flow<ApiResWraper<List<LotTypeModel>>> =
            repository.fetchLotType()
        submit(requestFlow, object : IApiResWrapper<ApiResWraper<List<LotTypeModel>>> {
            override fun onLoading(hasLoading: Boolean) {
                loadingLoginLiveData.postValue(hasLoading)
            }

            override fun onData(respondData: ApiResWraper<List<LotTypeModel>>) {
                dataListAllLotLiveDataLiveData.postValue(respondData)
            }

            override fun onError(message: String, code: Int, errorHashMap: JsonObject) {
                dataListAllLotLiveDataLiveData.postValue(ApiResWraper(code, message, false, errorHashMap))
            }

        })
    }

    /**** Check In *****/
    private var _submitCheckInMutableLiveData: MutableLiveData<ApiResWraper<TicketModel>> =
        MutableLiveData<ApiResWraper<TicketModel>>()

    val submitCheckInMutableLiveData get() = _submitCheckInMutableLiveData

    fun submitChecking(requestBody: HashMap<String, Any>) {
        val requestFlow: Flow<ApiResWraper<TicketModel>> =
            repository.submitChecking(requestBody)
        submit(requestFlow, object : IApiResWrapper<ApiResWraper<TicketModel>> {
            override fun onLoading(hasLoading: Boolean) {
                _loadingLoginLiveData.value = hasLoading
            }

            override fun onData(respondData: ApiResWraper<TicketModel>) {
                if (respondData.success) {
                    submitCheckInMutableLiveData.value = respondData
                }
            }

            override fun onError(
                message: String,
                code: Int,
                errorHashMap: JsonObject
            ) {
                submitCheckInMutableLiveData.value = ApiResWraper(
                    code,
                    message,
                    false,
                    errorHashMap,
                )
            }
        })

    }

}