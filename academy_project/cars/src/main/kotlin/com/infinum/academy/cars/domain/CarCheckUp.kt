package com.infinum.academy.cars.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "checkups")
data class CarCheckUp(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHECKUP_SEQ")
    @SequenceGenerator(name = "CHECKUP_SEQ", sequenceName = "CHECKUP_SEQ", allocationSize = 1)
    val id: Long = 0,

    val date_performed: LocalDateTime,

    val worker_name: String,

    val price: Float,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    val car: Car
)
