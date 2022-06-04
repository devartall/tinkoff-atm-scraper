package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class BottomLeft (

  @SerializedName("lat" ) var lat : Double? = null,
  @SerializedName("lng" ) var lng : Double? = null

)