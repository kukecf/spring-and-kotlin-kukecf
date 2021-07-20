package com.infinum.academy.cars.resource

import java.time.LocalDate

data class Car(
    val carId:Long,
    val ownerId: Long,
    val dateAdded: LocalDate,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Int,
    val serialNumber: String
)
