package com.infinum.academy.cars.repository

import java.time.LocalDateTime

data class CarCheckUp(
    val datePerformed: LocalDateTime,
    val workerName: String,
    val price: Float,
    val carId: Long
)
