package com.infinum.academy.cars.dto

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarInfo
import java.time.LocalDate

data class AddCarDto(
    val ownerId: Long,
    val productionYear: Int,
    val serialNumber: String,
    val manufacturerName: String,
    val modelName: String
)

fun AddCarDto.toCar(modelInfoFetcher: (String, String) -> CarInfo) = Car(
    ownerId = ownerId,
    dateAdded = LocalDate.now(),
    info = modelInfoFetcher.invoke(manufacturerName, modelName),
    productionYear = productionYear,
    serialNumber = serialNumber
)
