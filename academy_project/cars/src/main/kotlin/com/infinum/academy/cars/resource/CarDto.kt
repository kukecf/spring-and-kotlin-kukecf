package com.infinum.academy.cars.resource

import java.time.LocalDate

data class CarDto(
    val ownerId: Long,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Int,
    val serialNumber: String
)

fun CarDto.toDomainModel(id: Long) = Car(
    carId = id,
    ownerId = ownerId,
    dateAdded = LocalDate.now(),
    manufacturerName = manufacturerName,
    modelName = modelName,
    productionYear = productionYear,
    serialNumber = serialNumber
)
