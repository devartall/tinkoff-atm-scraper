package com.devartall.tinkoffatmscraper.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepo : CrudRepository<Subscription, Long> {
    fun findByChatId(chatId: Long): Subscription?
    fun findAllByEnabledIsTrue(): List<Subscription>
}