package com.daikou.p2parking.data.model

import com.google.gson.annotations.SerializedName

data class TicketModel(
    var timeIn : String? = null,
    var timeOut : String?  = null,
    var imgBase64 : String? = null,
    var amount : Double? = null,

    @SerializedName("id") var id: Int? = null,
    @SerializedName("ticket_no") var ticketNo: String? = null,
    @SerializedName("from_date") var fromDate: String? = null,

    ) : java.io.Serializable
