package com.infinum.academy.cars.domain

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "cars", uniqueConstraints = [UniqueConstraint(columnNames = ["serialNumber"])])
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_SEQ")
    @SequenceGenerator(name = "CAR_SEQ", sequenceName = "CAR_SEQ", allocationSize = 1)
    val id: Long = 0,

    val ownerId: Long,

    val dateAdded: LocalDate,

    val productionYear: Int,

    val serialNumber: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.REMOVE])
    val info: CarInfo
)
