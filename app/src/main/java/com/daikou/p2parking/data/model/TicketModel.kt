package com.daikou.p2parking.data.model

data class TicketModel(
    var ticketNo : String? = null,
    var timeIn : String? = null,
    var timeOut : String?  = null,
    var timeUse : String? = null,
    var image : String? = null,
    var amount : Double? = null
)
