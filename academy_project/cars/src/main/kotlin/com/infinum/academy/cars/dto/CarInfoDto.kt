package com.infinum.academy.cars.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.domain.CarInfoPrimaryKey


data class CarInfoDto(
    @JsonProperty("manufacturer") val manName: String,
    @JsonProperty("model_name") val modelName: String,
    @JsonProperty("is_common") val isCommon: Boolean,
)

data class CarResponse(
    @JsonProperty("data") val data: List<CarInfoDto>
)

fun CarInfoDto.toCarInfo(): CarInfo = CarInfo(carInfoPk = CarInfoPrimaryKey(manName, modelName), isCommon = isCommon)
