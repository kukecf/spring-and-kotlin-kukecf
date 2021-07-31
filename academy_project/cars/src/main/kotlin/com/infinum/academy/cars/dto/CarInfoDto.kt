package com.infinum.academy.cars.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.domain.CarInfoPrimaryKey

data class CarInfoDto(
    @JsonProperty("manufacturer") val manName: String,
    @JsonProperty("modelName") val modelName: String,
    @JsonProperty("isCommon") val isCommon: Boolean,
)

data class CarResponse(
    @JsonProperty("data") val data: List<CarInfoDto>
)

fun CarInfoDto.toCarInfo(): CarInfo = CarInfo(carInfoPk = CarInfoPrimaryKey(manName, modelName), isCommon = isCommon)
