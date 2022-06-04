package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class WorkPeriods (

  @SerializedName("openDay"   ) var openDay   : Int?    = null,
  @SerializedName("openTime"  ) var openTime  : String? = null,
  @SerializedName("closeDay"  ) var closeDay  : Int?    = null,
  @SerializedName("closeTime" ) var closeTime : String? = null

)