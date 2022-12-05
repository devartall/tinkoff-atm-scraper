package com.devartall.tinkoffatmscraper.service

import com.devartall.tinkoffatmscraper.dao.LastAtmInfo
import com.devartall.tinkoffatmscraper.dao.LastAtmInfoRepo
import com.devartall.tinkoffatmscraper.dao.SubscriptionRepo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NotificationScheduler(
    val atmService: AtmService,
    val telegramBotService: TelegramBotService,
    val subscriptionRepo: SubscriptionRepo,
    val lastAtmInfoRepo: LastAtmInfoRepo

) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(NotificationScheduler::class.java)
    }

    @Scheduled(cron = "\${app.tcs.atm.cron}")
    fun notifyAtmInfo() {
        val atmsInfo = atmService.getFullUsdAtms()
        val atmIdsInfo = atmsInfo.map { it.id }
        log.info("Получены данные по ${atmsInfo.size} банкоматам")

        val refilledAtmIds: List<String>
        val lastAtmInfo = lastAtmInfoRepo.findById(1L)
        if (lastAtmInfo.isEmpty) {
            refilledAtmIds = atmIdsInfo
            lastAtmInfoRepo.save(LastAtmInfo(atmIdsInfo))
            return
        } else {
            val lastAtmInfoPresent = lastAtmInfo.get()
            val lastAtmIdsInfo = lastAtmInfoPresent.atmIds.toSet()
            refilledAtmIds = atmIdsInfo.subtract(lastAtmIdsInfo).toList()
            lastAtmInfoPresent.atmIds = atmIdsInfo
            lastAtmInfoRepo.save(lastAtmInfoPresent)
        }

        val subscriptionList = subscriptionRepo.findAllByEnabledIsTrue()

        if (subscriptionList.isEmpty()) return

        subscriptionList.forEach { subscription ->
            val subAtmIds = if (subscription.atmIds.isNullOrEmpty()) refilledAtmIds else subscription.atmIds!!
            val subRefilledAtmIds = refilledAtmIds.intersect(subAtmIds.toSet())
            if (subRefilledAtmIds.isNotEmpty()) {
                telegramBotService.notifyAtmStatus(
                    subscription.chatId,
                    atmsInfo.filter { it.id in subRefilledAtmIds },
                )
                log.info("Отправка уведомления chatId=${subscription.chatId} fullName=${subscription.fullName} отправлена!")
            }
        }
    }
}