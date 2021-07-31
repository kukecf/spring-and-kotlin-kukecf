package com.infinum.academy.cars.dto

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarInfo
import java.time.LocalDate

data class AddCarDto(
    val ownerId: Long,
    val productionYear: Int,
    val serialNumber: String,
    val manufacturerName: String,
    val modelName:String
)

fun AddCarDto.toCar(modelInfoFetcher: (String, String) -> CarInfo) = Car(
    owner_id = ownerId,
    date_added = LocalDate.now(),
    info = modelInfoFetcher.invoke(manufacturerName, modelName),
    production_year = productionYear,
    serial_number = serialNumber
)
