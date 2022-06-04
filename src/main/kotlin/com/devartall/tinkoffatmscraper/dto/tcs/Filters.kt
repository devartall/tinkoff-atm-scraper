package com.devartall.tinkoffatmscraper.dto.tcs

import com.google.gson.annotations.SerializedName


data class Filters(

        @SerializedName("banks") var banks: ArrayList<String> = arrayListOf(),
        @SerializedName("showUnavailable") var showUnavailable: Boolean? = null,
        @SerializedName("currencies") var currencies: ArrayList<String> = arrayListOf()

)