package com.devartall.tinkoffatmscraper.dao

import java.time.LocalDateTime
import javax.persistence.*


@Entity
class Subscription(
    var chatId: Long,
    var fullName: String? = null,
    @Column(length = 4000)
    @Convert(converter = StringListConverter::class)
    var atmIds: List<String>? = null,
    var enabled: Boolean,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,

    @Id @GeneratedValue var id: Long? = null
)

