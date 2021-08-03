package com.infinum.academy.cars.dto

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import java.time.LocalDateTime

data class AddCarCheckUpDto(
    val workerName: String,
    val price: Float,
    val carId: Long,
    val date: LocalDateTime
)

fun AddCarCheckUpDto.toCarCheckUp(carFetcher: (Long) -> Car) = CarCheckUp(
    datePerformed = date,
    workerName = workerName,
    price = price,
    car = carFetcher.invoke(this.carId)
)

