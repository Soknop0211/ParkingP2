package com.daikou.p2parking.data.model

import com.google.gson.annotations.SerializedName

data class TicketModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("ticket_no") var ticketNo: String? = null,
    @SerializedName("from_date") var fromDate: String? = null,
    @SerializedName("to_date") var toDate: String? = null,
    @SerializedName("duration") var duration: String? = null,
    @SerializedName("total_price") var totalPrice: Double? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("payment_link") var paymentLink: String? = null,
    ) : java.io.Serializable
