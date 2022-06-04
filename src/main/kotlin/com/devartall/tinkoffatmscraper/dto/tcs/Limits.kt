package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class Limits (

  @SerializedName("currency"                ) var currency                : String?        = null,
  @SerializedName("amount"                  ) var amount                  : Int?           = null,
  @SerializedName("withdrawMaxAmount"       ) var withdrawMaxAmount       : Int?           = null,
  @SerializedName("depositionMaxAmount"     ) var depositionMaxAmount     : Int?           = null,
  @SerializedName("depositionMinAmount"     ) var depositionMinAmount     : Int?           = null,
  @SerializedName("withdrawDenominations"   ) var withdrawDenominations   : ArrayList<Int> = arrayListOf(),
  @SerializedName("depositionDenominations" ) var depositionDenominations : ArrayList<Int> = arrayListOf(),
  @SerializedName("overTrustedLimit"        ) var overTrustedLimit        : Boolean?       = null

)