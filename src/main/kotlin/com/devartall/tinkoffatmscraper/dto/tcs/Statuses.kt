package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class Statuses (

  @SerializedName("criticalFailure"       ) var criticalFailure       : Boolean? = null,
  @SerializedName("qrOperational"         ) var qrOperational         : Boolean? = null,
  @SerializedName("nfcOperational"        ) var nfcOperational        : Boolean? = null,
  @SerializedName("cardReaderOperational" ) var cardReaderOperational : Boolean? = null,
  @SerializedName("cashInAvailable"       ) var cashInAvailable       : Boolean? = null

)