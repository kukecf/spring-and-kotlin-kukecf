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

    @Column(name = "date_performed")
    val datePerformed: LocalDateTime,

    @Column(name = "worker_name")
    val workerName: String,

    val price: Float,

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.REMOVE])
    val car: Car
)
