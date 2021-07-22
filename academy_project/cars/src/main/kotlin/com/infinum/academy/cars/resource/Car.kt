package com.infinum.academy.cars.resource

import java.time.LocalDate

data class Car(
    val carId: Long = 0,
    val ownerId: Long,
    val dateAdded: LocalDate,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Int,
    val serialNumber: String,
    val checkUps: List<CarCheckUp> = emptyList()
)
