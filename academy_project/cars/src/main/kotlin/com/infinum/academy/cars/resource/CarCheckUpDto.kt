package com.infinum.academy.cars.resource

import java.time.LocalDateTime

data class CarCheckUpDto(
    val workerName: String,
    val price: Float,
    val carId: Long
)

fun CarCheckUpDto.toDomainModel() = CarCheckUp(
    datePerformed = LocalDateTime.now(),
    workerName = workerName,
    price = price,
    carId = carId
)

