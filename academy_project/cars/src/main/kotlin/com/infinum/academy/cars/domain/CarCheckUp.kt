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

    val datePerformed: LocalDateTime,

    val workerName: String,

    val price: Float,

    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "car_id", nullable = false)
    val carId: Long
    //val car: Car
)
