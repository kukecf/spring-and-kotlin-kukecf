package com.infinum.academy.cars.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import java.time.LocalDate

data class CarDto(
    val id: Long = 0,

    val ownerId: Long,

    val dateAdded: LocalDate,

    val manufacturerName: String,

    val modelName: String,

    val productionYear: Int,

    val serialNumber: String,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val checkups: List<CheckUpDto> = emptyList()
){
    constructor(car: Car, checkups: List<CarCheckUp>) : this (
    car.id,
    car.owner_id,
    car.date_added,
    car.manufacturer_name,
    car.model_name,
    car.production_year,
    car.serial_number,
    checkups.map{CheckUpDto(it)}
    )

    constructor(car:Car) : this(car,emptyList())
}
