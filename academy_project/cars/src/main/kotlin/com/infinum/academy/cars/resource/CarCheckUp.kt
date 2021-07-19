package com.infinum.academy.cars.resource

import java.time.LocalDateTime

data class CarCheckUp(
    val checkUpId: Long,
    val datePerformed: LocalDateTime,
    val workerName: String,
    val price: Float,
    val carId: Long
)
