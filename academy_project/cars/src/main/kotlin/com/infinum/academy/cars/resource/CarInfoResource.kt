package com.infinum.academy.cars.resource

import com.fasterxml.jackson.annotation.JsonProperty
import com.infinum.academy.cars.domain.CarInfo
import org.springframework.hateoas.RepresentationModel

data class CarInfoResource(
    val manName: String,
    val modelName: String,
    val isCommon: Boolean
) : RepresentationModel<CarInfoResource>(){
    constructor(info: CarInfo) : this(
        manName=info.carInfoPk.manufacturer,
        modelName=info.carInfoPk.modelName,
        isCommon=info.isCommon
    )

}