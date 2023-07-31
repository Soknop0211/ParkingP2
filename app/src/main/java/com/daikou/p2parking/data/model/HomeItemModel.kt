package com.daikou.p2parking.data.model

import com.daikou.p2parking.emunUtil.HomeScreenEnum

data class HomeItemModel(
    var imageLogo : Int? = null,
    var title : String? = null,
    var action : HomeScreenEnum
)
