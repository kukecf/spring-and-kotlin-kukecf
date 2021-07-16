package com.infinum.academy.cars.repository

import java.lang.RuntimeException

interface BodyShopRepository {
    fun insertCar(car: Car): Long
    fun getCar(id: Long): Car
    fun registerCheckUp(checkUp: CarCheckUp): Long
    fun getCarCheckUp(id: Long): CarCheckUp
    fun hasCarWithId(id: Long): Boolean
    fun hasCarWithSerialNumber(serialNo: String): Boolean
}

class CarNotFoundException(message: String) : RuntimeException(message)
class CarCheckUpNotFoundException(message: String) : RuntimeException(message)