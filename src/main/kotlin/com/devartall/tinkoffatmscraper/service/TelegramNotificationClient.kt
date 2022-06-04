package com.devartall.tinkoffatmscraper.service

import com.devartall.tinkoffatmscraper.dto.AtmDtoFilteredRs
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

private const val API_BOT_URI = "https://api.telegram.org/bot$TCS_BOT_TOKEN/sendMessage"

@Component
class TelegramNotificationClient(
    val tgRestTemplate: RestTemplate
) {
    fun notifyAtmStatus(chatId: Long, refilledAtms: List<AtmDtoFilteredRs>, decreasedAtmIds: Set<String>): String? {
        val urlTemplate = UriComponentsBuilder.fromHttpUrl(API_BOT_URI)
            .queryParam("chat_id", "{chat_id}")
            .queryParam("text", "{text}")
            .queryParam("parse_mode", "{parse_mode}")
            .encode()
            .toUriString()

        val uriVars = HashMap<String, String>()
        uriVars["chat_id"] = chatId.toString()
        uriVars["parse_mode"] = "MarkdownV2"
        var text =
            "Баланс стал больше 5000 USD\n" + refilledAtms.joinToString(separator = "\n\n") { "${it.id} ${it.address}" }

        if (decreasedAtmIds.isNotEmpty()) {
            text = "$text\n\nВ банкоматах с id:$decreasedAtmIds баланс стал меньше 5000 USD"
        }
        val replaced = text
            .replace(".", "\\.")
            .replace("*", "\\*")
            .replace("-", "\\-")
            .replace("_", "\\_")
        uriVars["text"] = replaced.replace("||", "*")

        return tgRestTemplate.getForObject(urlTemplate, String::class.java, uriVars)
    }
}

@Configuration
class TgClientConfiguration {
    @Bean
    fun tgRestTemplate() = RestTemplate()
}