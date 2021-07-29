package com.infinum.academy.cars.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.domain.CarInfoPrimaryKey

data class CarInfoDto(
    @JsonProperty("manufacturer") val man_name: String,
    @JsonProperty("model_name") val model_name: String,
    @JsonProperty("is_common") val is_common: Boolean,
)

data class CarResponse(
    @JsonProperty("data") val data: List<CarInfoDto>
)

fun CarInfoDto.toCarInfo(): CarInfo = CarInfo(carInfoPk = CarInfoPrimaryKey(man_name, model_name), isCommon = is_common)
