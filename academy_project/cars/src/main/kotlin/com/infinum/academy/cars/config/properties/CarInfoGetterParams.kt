package com.infinum.academy.cars.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "car.info")
@ConstructorBinding
data class CarInfoGetterParams(
    val baseUrl: String,
    val scheduleInterval: String
)
