package com.infinum.academy.cars.dto

import com.infinum.academy.cars.domain.Car
import java.time.LocalDate

data class CarDto(
    val ownerId: Long,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Int,
    val serialNumber: String
)

fun CarDto.toCarCheckUp() = Car(
    ownerId = ownerId,
    dateAdded = LocalDate.now(),
    manufacturerName = manufacturerName,
    modelName = modelName,
    productionYear = productionYear,
    serialNumber = serialNumber
)
