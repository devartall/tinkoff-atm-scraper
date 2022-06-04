package com.devartall.tinkoffatmscraper

import com.devartall.tinkoffatmscraper.dto.AtmDtoFilteredRs
import com.devartall.tinkoffatmscraper.service.AtmService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(val service: AtmService) {

    @PostMapping("/post")
    fun post(): List<AtmDtoFilteredRs> {
        return service.getAtmInfo()
    }
}