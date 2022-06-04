package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class Brand (

  @SerializedName("id"          ) var id          : String?  = null,
  @SerializedName("name"        ) var name        : String?  = null,
  @SerializedName("logoFile"    ) var logoFile    : String?  = null,
  @SerializedName("roundedLogo" ) var roundedLogo : Boolean? = null

)