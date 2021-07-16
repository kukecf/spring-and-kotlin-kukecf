package com.infinum.academy.cars.repository

import java.time.LocalDate

data class Car(
    val ownerId: Long,
    val dateAdded: LocalDate,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Int,
    val serialNumber: String,
    val checkUps: MutableList<CarCheckUp>
)
