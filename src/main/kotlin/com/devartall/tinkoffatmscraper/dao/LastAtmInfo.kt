package com.devartall.tinkoffatmscraper.dao

import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
class LastAtmInfo(
    @Column(length = 4000)
    @Convert(converter = StringListConverter::class)
    var atmIds: List<String> = emptyList(),

    @Id var id: Long = 1L
)

interface LastAtmInfoRepo : CrudRepository<LastAtmInfo, Long>