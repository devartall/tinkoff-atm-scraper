package com.devartall.tinkoffatmscraper.service

import com.devartall.tinkoffatmscraper.dto.*
import com.devartall.tinkoffatmscraper.dto.tcs.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AtmService(
    val client: AtmClient,
    val converter: AtmConverter,

    @Value("\${app.tcs.atm.bottom-left-lat}") val bottomLeftLat: Double,
    @Value("\${app.tcs.atm.bottom-left-lng}") val bottomLeftLng: Double,
    @Value("\${app.tcs.atm.top-right-lat}") val topRightLat: Double,
    @Value("\${app.tcs.atm.top-right-lng}") val topRightLng: Double,
    @Value("\${app.tcs.atm.zoom}") val zoom: Int
) {

    fun getAtmInfo(): List<AtmDtoFilteredRs> {
        val rq = creatAtmInfo()
        val atmInfo = client.getAtmInfo(rq)

        return converter.convert(atmInfo)
            .filter { it.limits.first { limitDtoRs -> limitDtoRs.currency == "USD" }.amount == 5000 }
    }

    private fun creatAtmInfo(): AtmDtoRq {
        val bottomLeft = BottomLeft(lat = bottomLeftLat, lng = bottomLeftLng)
        val topRight = TopRight(lat = topRightLat, lng = topRightLng)
        val bounds = Bounds(bottomLeft = bottomLeft, topRight = topRight)

        val filters = Filters(banks = arrayListOf("tcs"), showUnavailable = true, currencies = arrayListOf("USD"))

        return AtmDtoRq(bounds, filters, zoom)
    }
}