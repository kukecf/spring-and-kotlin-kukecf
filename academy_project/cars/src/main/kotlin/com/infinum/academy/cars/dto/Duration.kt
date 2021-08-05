package com.infinum.academy.cars.dto

import java.time.Period

enum class Duration {
    ONE_WEEK,
    ONE_MONTH,
    SIX_MONTHS;

    fun toPeriod(): Period {
        return when (this) {
            ONE_WEEK -> Period.ofWeeks(1)
            ONE_MONTH -> Period.ofMonths(1)
            SIX_MONTHS -> Period.ofMonths(6)
        }
    }
}
