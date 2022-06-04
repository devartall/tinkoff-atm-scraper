package com.devartall.tinkoffatmscraper.service

import com.devartall.tinkoffatmscraper.dto.AtmDtoFilteredRs
import com.devartall.tinkoffatmscraper.dto.tcs.AtmDtoRs
import com.devartall.tinkoffatmscraper.dto.LimitDtoRs
import com.devartall.tinkoffatmscraper.dto.tcs.Limits
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class AtmConverter {
    fun convert(source: AtmDtoRs): List<AtmDtoFilteredRs> {
        return source.payload!!.clusters.flatMap { it.points }
            .map { AtmDtoFilteredRs(id = it.id!!, it.address!!, convertLimits(it.limits)) }
    }

    private fun convertLimits(limits: ArrayList<Limits>): List<LimitDtoRs> {
        return limits.map { LimitDtoRs(it.currency!!, it.amount!!) }
    }
}