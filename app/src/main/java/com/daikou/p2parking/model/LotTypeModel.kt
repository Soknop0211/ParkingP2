package com.daikou.p2parking.model

import com.google.gson.annotations.SerializedName

data class LotTypeModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
) : java.io.Serializable