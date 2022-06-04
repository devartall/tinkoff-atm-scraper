package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class Bounds (

    @SerializedName("bottomLeft" ) var bottomLeft : BottomLeft? = BottomLeft(),
    @SerializedName("topRight"   ) var topRight   : TopRight?   = TopRight()

)