package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class AtmInfo (

    @SerializedName("available"  ) var available  : Boolean?          = null,
    @SerializedName("isTerminal" ) var isTerminal : Boolean?          = null,
    @SerializedName("statuses"   ) var statuses   : Statuses?         = Statuses(),
    @SerializedName("limits"     ) var limits     : ArrayList<Limits> = arrayListOf()

)