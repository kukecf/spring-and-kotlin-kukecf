package com.infinum.academy.cars.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonIncludeProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import java.time.LocalDateTime

data class AddSchedCheckUpDto(
    val workerName: String,
    val price: Float,
    val carId: Long,
    val duration: Duration = Duration.ONE_MONTH,
)

//ne znam koliko je ovo elegantno, ali radi!

fun AddSchedCheckUpDto.toCarCheckUp(carFetcher: (Long) -> Car): CarCheckUp {
    parseDuration(duration).let {
        return CarCheckUp(
            datePerformed = LocalDateTime.now().plus(it.quant.toLong(), it.unit),
            workerName = workerName,
            price = price,
            car = carFetcher.invoke(this.carId)
        )
    }
}
