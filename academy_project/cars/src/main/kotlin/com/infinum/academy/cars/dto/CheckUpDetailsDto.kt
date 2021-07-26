package com.infinum.academy.cars.dto

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import java.time.LocalDateTime

data class CheckUpDetailsDto(
    val id: Long = 0,

    val datePerformed: LocalDateTime,

    val workerName: String,

    val price: Float
){
    constructor(checkup:CarCheckUp) : this (
        checkup.id,
        checkup.datePerformed,
        checkup.workerName,
        checkup.price,
    )
}
