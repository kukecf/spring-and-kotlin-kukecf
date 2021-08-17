package com.infinum.academy.cars.services

import com.infinum.academy.cars.domain.CarInfo

interface CarInfoService {
    fun getModelsFromServer(): List<CarInfo>?
}
