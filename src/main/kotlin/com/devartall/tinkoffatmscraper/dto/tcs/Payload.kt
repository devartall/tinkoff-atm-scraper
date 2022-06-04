package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class Payload (

    @SerializedName("hash"     ) var hash     : String?             = null,
    @SerializedName("zoom"     ) var zoom     : Int?                = null,
    @SerializedName("bounds"   ) var bounds   : Bounds?             = Bounds(),
    @SerializedName("clusters" ) var clusters : ArrayList<Clusters> = arrayListOf()

)