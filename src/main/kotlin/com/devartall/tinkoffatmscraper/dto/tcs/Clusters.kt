package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class Clusters (

    @SerializedName("id"     ) var id     : String?           = null,
    @SerializedName("hash"   ) var hash   : String?           = null,
    @SerializedName("bounds" ) var bounds : Bounds?           = Bounds(),
    @SerializedName("center" ) var center : Center?           = Center(),
    @SerializedName("points" ) var points : ArrayList<Points> = arrayListOf()

)