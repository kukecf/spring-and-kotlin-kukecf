package com.infinum.academy.cars.repository

import java.time.LocalDate

data class CarDto(
    val ownerId: Long,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Int,
    val serialNumber: String,
)
