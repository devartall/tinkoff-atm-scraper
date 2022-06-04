package com.devartall.tinkoffatmscraper.dto

data class AtmDtoFilteredRs(
    val id: String,
    val address: String,
    val limits: List<LimitDtoRs>
)

data class LimitDtoRs(
    val currency: String,
    val amount: Int
)