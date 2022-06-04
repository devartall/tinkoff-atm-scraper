package com.devartall.tinkoffatmscraper.service

import com.devartall.tinkoffatmscraper.dto.tcs.AtmDtoRq
import com.devartall.tinkoffatmscraper.dto.tcs.AtmDtoRs
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject

private const val TCS_API_URI = "https://api.tinkoff.ru/geo/withdraw/clusters"

@Component
class AtmClient {

    fun getAtmInfo(rq: AtmDtoRq): AtmDtoRs {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.add("method", "POST")
        headers.add("authority", "api.tinkoff.ru")
        val request: HttpEntity<AtmDtoRq> = HttpEntity(rq, headers)
        return restTemplate.postForObject(TCS_API_URI, request, AtmDtoRs::class)
    }
}