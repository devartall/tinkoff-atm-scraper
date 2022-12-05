package com.devartall.tinkoffatmscraper.api.admin

import com.devartall.tinkoffatmscraper.dto.AtmDtoFilteredRs
import com.devartall.tinkoffatmscraper.dto.LimitDtoRs
import com.devartall.tinkoffatmscraper.service.AtmService
import com.devartall.tinkoffatmscraper.service.TelegramBotService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RestAdminController(
    @Value("\${app.admin.secret}") val secret: String,
    val atmService: AtmService,
    val telegramService: TelegramBotService
) {

    @GetMapping("/atms")
    fun getAtmInfo(@RequestParam secret: String): ResponseEntity<List<AtmDtoFilteredRs>> {
        if (secret != this.secret) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()

        return ResponseEntity.ok(atmService.getFullUsdAtms())
    }

    @PostMapping("/bot/text")
    fun sendBotTextMessage(@RequestParam secret: String, @RequestParam chatId: Long): ResponseEntity<String> {
        if (secret != this.secret) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()

        return ResponseEntity.ok(telegramService.notifyAtmStatus(chatId, getAtms()))
    }

    private fun getAtms(): List<AtmDtoFilteredRs> {
        return listOf(
            AtmDtoFilteredRs("1234567890", "Moscow, Ulitsa Pushikina, dom Kolotushkina", emptyList()),
            AtmDtoFilteredRs("2", "Печальная область, Тоскливый район, город Грусть, проспект Разочарования, д. 13", emptyList()),
            AtmDtoFilteredRs("3", "test", listOf(
                LimitDtoRs("USD", 5000),
                LimitDtoRs("EUR", 4500),
                LimitDtoRs("RUB", 100)
            ))
        )
    }
}