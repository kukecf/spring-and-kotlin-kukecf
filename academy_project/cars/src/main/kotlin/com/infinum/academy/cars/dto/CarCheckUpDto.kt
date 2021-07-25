package com.infinum.academy.cars.dto

import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.repository.CarCheckUpRepository
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
