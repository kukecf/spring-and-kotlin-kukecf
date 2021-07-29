package com.infinum.academy.cars.dto

import com.infinum.academy.cars.domain.Car
import java.time.LocalDate

data class AddCarDto(
    val ownerId: Long,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Int,
    val serialNumber: String
)

fun AddCarDto.toCar() = Car(
    owner_id = ownerId,
    date_added = LocalDate.now(),
    manufacturer_name = manufacturerName,
    model_name = modelName,
    production_year = productionYear,
    serial_number = serialNumber
)
