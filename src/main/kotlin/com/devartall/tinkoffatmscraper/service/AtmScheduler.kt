package com.devartall.tinkoffatmscraper.service

import com.devartall.tinkoffatmscraper.dao.LastAtmInfo
import com.devartall.tinkoffatmscraper.dao.LastAtmInfoRepo
import com.devartall.tinkoffatmscraper.dao.SubscriptionRepo
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AtmScheduler(
    val service: AtmService,
    val tgNotification: TelegramNotificationClient,
    val subscriptionRepo: SubscriptionRepo,
    val lastAtmInfoRepo: LastAtmInfoRepo
) {
    companion object {
        val log = LoggerFactory.getLogger(AtmScheduler::class.java)
    }

    @Scheduled(cron = "\${app.tcs.atm.cron}")
    fun notifyAtmInfo() {
        val atmInfo = service.getAtmInfo()
        val atmIdsInfo = atmInfo.map { it.id }
        log.info("Получены данные по ${atmInfo.size} банкоматам")

        val refilledAtmIds: List<String>
        val decreasedAtmsIds: List<String>
        val lastAtmInfo = lastAtmInfoRepo.findById(1L)
        if (lastAtmInfo.isEmpty) {
            refilledAtmIds = atmIdsInfo
            decreasedAtmsIds = emptyList()
            lastAtmInfoRepo.save(LastAtmInfo(atmIdsInfo))
        } else {
            val lastAtmInfoPresent = lastAtmInfo.get()
            val lastAtmIdsInfo = lastAtmInfoPresent.atmIds.toSet()
            refilledAtmIds = atmIdsInfo.subtract(lastAtmIdsInfo).toList()
            decreasedAtmsIds = lastAtmIdsInfo.subtract(atmIdsInfo.toSet()).toList()
            lastAtmInfoPresent.atmIds = atmIdsInfo
            lastAtmInfoRepo.save(lastAtmInfoPresent)
        }

        val subscriptionList = subscriptionRepo.findAllByEnabledIsTrue()

        if (subscriptionList.isEmpty()) return

        subscriptionList.forEach { subscription ->
            val subAtmIds = (subscription.atmIds ?: emptyList()).toSet()
            val subRefilledAtmIds = refilledAtmIds.intersect(subAtmIds)
            val subDecreasedAtmIds = decreasedAtmsIds.intersect(subAtmIds)
            val intersect = subRefilledAtmIds.plus(subDecreasedAtmIds)
            if (intersect.isNotEmpty()) {
                tgNotification.notifyAtmStatus(
                    subscription.chatId,
                    atmInfo.filter { it.id in subRefilledAtmIds },
                    subDecreasedAtmIds
                )
                log.info("Отправка уведомления chatId=${subscription.chatId} fullName=${subscription.fullName} отправлена!")
            }
        }
    }
}