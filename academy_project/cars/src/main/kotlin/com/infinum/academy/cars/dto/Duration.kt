package com.infinum.academy.cars.dto

import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

enum class Duration {
    ONE_WEEK,
    ONE_MONTH,
    SIX_MONTHS
}

fun parseDuration(duration:Duration) : TimeInterval {
    return when(duration){
        Duration.ONE_WEEK -> TimeInterval(1,ChronoUnit.WEEKS)
        Duration.ONE_MONTH -> TimeInterval(1,ChronoUnit.MONTHS)
        Duration.SIX_MONTHS -> TimeInterval(6,ChronoUnit.MONTHS)
    }
}

data class TimeInterval(
    val quant : Int,
    val unit : ChronoUnit
)