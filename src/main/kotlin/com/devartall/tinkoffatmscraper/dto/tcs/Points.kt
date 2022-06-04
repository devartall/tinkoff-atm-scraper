package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class Points (

    @SerializedName("id"           ) var id           : String?                = null,
    @SerializedName("brand"        ) var brand        : Brand?                 = Brand(),
    @SerializedName("pointType"    ) var pointType    : String?                = null,
    @SerializedName("location"     ) var location     : Location?              = Location(),
    @SerializedName("address"      ) var address      : String?                = null,
    @SerializedName("phone"        ) var phone        : ArrayList<String>      = arrayListOf(),
    @SerializedName("limits"       ) var limits       : ArrayList<Limits>      = arrayListOf(),
    @SerializedName("workPeriods"  ) var workPeriods  : ArrayList<WorkPeriods> = arrayListOf(),
    @SerializedName("installPlace" ) var installPlace : String?                = null,
    @SerializedName("atmInfo"      ) var atmInfo      : AtmInfo?               = AtmInfo()

)