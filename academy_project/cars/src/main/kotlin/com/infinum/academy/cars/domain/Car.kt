package com.infinum.academy.cars.domain

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "cars")
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_SEQ")
    @SequenceGenerator(name = "CAR_SEQ", sequenceName = "CAR_SEQ", allocationSize = 1)
    val id: Long = 0,

    val ownerId: Long,

    val dateAdded: LocalDate,

    val manufacturerName: String,

    val modelName: String,

    val productionYear: Int,

    val serialNumber: String,

    @ElementCollection
    @CollectionTable(name = "car_checkups", joinColumns = [JoinColumn(name = "carId")])
    val checkUps: List<CarCheckUp> = emptyList()
)
