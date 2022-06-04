package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class AtmDtoRs (

    @SerializedName("trackingId" ) var trackingId : String?  = null,
    @SerializedName("payload"    ) var payload    : Payload? = Payload(),
    @SerializedName("time"       ) var time       : String?  = null,
    @SerializedName("status"     ) var status     : String?  = null

)