package com.infinum.academy.cars.dto

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import java.time.LocalDate

data class CarDetailsDto(
    val id: Long = 0,

    val ownerId: Long,

    val dateAdded: LocalDate,

    val manufacturerName: String,

    val modelName: String,

    val productionYear: Int,

    val serialNumber: String,

    val checkups: List<CheckUpDetailsDto>
){
    constructor(car: Car, checkups: List<CarCheckUp>) : this (
    car.id,
    car.ownerId,
    car.dateAdded,
    car.manufacturerName,
    car.modelName,
    car.productionYear,
    car.serialNumber,
    checkups.map{CheckUpDetailsDto(it)}
    )
}
