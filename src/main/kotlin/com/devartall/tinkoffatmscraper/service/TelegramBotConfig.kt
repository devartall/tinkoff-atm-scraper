package com.devartall.tinkoffatmscraper.service

import com.devartall.tinkoffatmscraper.dao.Subscription
import com.devartall.tinkoffatmscraper.dao.SubscriptionRepo
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Configuration
class TelegramBotConfig(
    @Value("\${app.telegram.bot.token}") val botToken: String
) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(TelegramBotConfig::class.java)

        val startMessage = """
            Приветствую!            
            Включение уведомлений: /on
            Отключение уведомлений: /off
            
            Для получения уведомлений для определенных банкоматов напишите id банкоматов через запятую. Пример:
            
            002583,002322,003057

            id банкоматов можно найти на сайте https://www.tinkoff.ru/maps/atm выбрав отдельный банкомат (скриншот ниже)
            
            Текущие ограничения:
            - Только пополнение USD
            - Только Московский банкоматы
            - Обновления раз в 5 минут           
        """.trimIndent()
    }

    @Bean
    fun tgBot(subscriptionRepo: SubscriptionRepo): Bot {
        val bot = bot {
            token = botToken
            dispatch {
                command("start") {
                    log.info("start ${message.chat.id} ${getFullName(message)}")
                    try {
                        val subscription = subscriptionRepo.findByChatId(message.chat.id)
                        val now = LocalDateTime.now()
                        if (subscription == null) {
                            subscriptionRepo.save(
                                Subscription(
                                    chatId = message.chat.id,
                                    fullName = getFullName(message),
                                    enabled = false,
                                    createdAt = now,
                                    updatedAt = now
                                )
                            )
                        } else {
                            subscription.updatedAt = now
                            subscriptionRepo.save(subscription)
                        }

                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = startMessage
                        )
                        bot.forwardMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            fromChatId = ChatId.fromId(94661935L),
                            messageId = 3
                        )

                    } catch (e: Exception) {
                        log.error("Не удалось выполнить команду /start", e)
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = "Не удалось выполнить действие"
                        )
                    }

                }

                command("on") {
                    log.info("on ${message.chat.id} ${getFullName(message)}")
                    try {
                        val subscription = subscriptionRepo.findByChatId(message.chat.id)
                        if (subscription == null) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(message.chat.id),
                                text = "Не удалось начать получение уведомлений. Начните с команды /start"
                            )
                        } else {
                            subscription.enabled = true
                            subscription.updatedAt = LocalDateTime.now()
                            subscriptionRepo.save(subscription)
                            bot.sendMessage(
                                chatId = ChatId.fromId(message.chat.id),
                                text = "Уведомления успешно включены для id: ${subscription.atmIds}"
                            )
                        }
                    } catch (e: Exception) {
                        log.error("Не удалось выполнить команду /on", e)
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = "Не удалось выполнить действие"
                        )
                    }
                }

                command("off") {
                    log.info("off ${message.chat.id} ${getFullName(message)}")
                    try {
                        val subscription = subscriptionRepo.findByChatId(message.chat.id)
                        if (subscription == null) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(message.chat.id),
                                text = "Не удалось выключить уведомления. Начните с команды /start"
                            )
                        } else {
                            subscription.enabled = false
                            subscription.updatedAt = LocalDateTime.now()
                            subscriptionRepo.save(subscription)
                            bot.sendMessage(
                                chatId = ChatId.fromId(message.chat.id),
                                text = "Уведомления успешно отключены"
                            )
                        }
                    } catch (e: Exception) {
                        log.error("Не удалось выполнить команду /off", e)
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = "Не удалось выполнить действие"
                        )
                    }
                }

                text {
                    try {
                        val textMessage = message.text
                        if (listOf("/start", "/on", "/off").contains(textMessage)) return@text
                        log.info("text ${message.chat.id} ${getFullName(message)} ${message.text}")

                        val atmIds: List<String>? = parseText(textMessage)

                        if (atmIds == null) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(message.chat.id),
                                text = "Не удалось распознать текст. Введи id через запятую.\nПример: 002241,003355,002935"
                            )
                        } else {
                            val subscription = subscriptionRepo.findByChatId(message.chat.id)
                            if (subscription == null) {
                                bot.sendMessage(
                                    chatId = ChatId.fromId(message.chat.id),
                                    text = "Не удалось начать получение уведомлений. Начните с команды /start или обратитсь к владельцу бота @ArturAllayarov"
                                )
                            } else {
                                subscription.atmIds = atmIds
                                subscription.updatedAt = LocalDateTime.now()
                                subscription.enabled = true
                                subscriptionRepo.save(subscription)
                                bot.sendMessage(
                                    chatId = ChatId.fromId(message.chat.id),
                                    text = "Уведомления успешно включены для id: $atmIds"
                                )
                            }
                        }
                    } catch (e: Exception) {
                        log.error("Не удалось обработать сообщение!", e)
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = "Не удалось обработать сообщение!"
                        )
                    }
                }
            }
        }
        bot.startPolling()
        return bot
    }

    private fun parseText(textMessage: String?): List<String>? {
        if (textMessage == null) return null

        val atmIds = textMessage.trim().split(",")

        return atmIds.ifEmpty { null }
    }

    private fun getFullName(message: Message): String? {
        val sb = StringBuilder()

        message.from?.firstName.let { sb.append("$it ") }
        message.from?.lastName.let { sb.append("$it ") }
        message.from?.username.let { sb.append("($it)") }

        return if (sb.isBlank()) null else sb.toString()

    }

    @Bean
    fun telegramRestTemplate() = RestTemplate()
}