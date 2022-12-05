package com.devartall.tinkoffatmscraper.service

import com.devartall.tinkoffatmscraper.dto.AtmDtoFilteredRs
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


@Component
class TelegramBotService(
    @Value("\${app.telegram.bot.token}") val botToken: String,
    val telegramRestTemplate: RestTemplate
) {
    fun notifyAtmStatus(chatId: Long, refilledAtms: List<AtmDtoFilteredRs>): String? {
        val urlTemplate = UriComponentsBuilder.fromHttpUrl("https://api.telegram.org/bot$botToken/sendMessage")
            .queryParam("chat_id", "{chat_id}")
            .queryParam("text", "{text}")
            .queryParam("parse_mode", "{parse_mode}")
            .encode()
            .toUriString()

        val uriVars = HashMap<String, String>()
        uriVars["chat_id"] = chatId.toString()
        uriVars["parse_mode"] = "MarkdownV2"
        val text =
            "Баланс пополнен:\n" + refilledAtms.joinToString(separator = "\n\n") { "ID:${it.id} Адрес:${it.address}" }

        val replaced = text
            .replace(".", "\\.")
            .replace("*", "\\*")
            .replace("-", "\\-")
            .replace("_", "\\_")
        uriVars["text"] = replaced.replace("||", "*")

        return telegramRestTemplate.getForObject(urlTemplate, String::class.java, uriVars)
    }
}