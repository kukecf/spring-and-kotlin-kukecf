package com.infinum.academy.cars.dto

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarRepository
import java.time.LocalDateTime

data class CarCheckUpDto(
    val workerName: String,
    val price: Float,
    val carId: Long
)

fun CarCheckUpDto.toCarCheckUp(carFetcher:(Long)->Car) = CarCheckUp(
    datePerformed = LocalDateTime.now(),
    workerName = workerName,
    price = price,
    car = carFetcher.invoke(this.carId)
)

