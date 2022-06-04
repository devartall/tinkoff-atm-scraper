package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class AtmDtoRq (

    @SerializedName("bounds"  ) var bounds  : Bounds?  = Bounds(),
    @SerializedName("filters" ) var filters : Filters? = Filters(),
    @SerializedName("zoom"    ) var zoom    : Int?     = null

)