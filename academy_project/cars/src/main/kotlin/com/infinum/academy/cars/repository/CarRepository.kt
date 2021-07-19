package com.infinum.academy.cars.repository

import com.infinum.academy.cars.resource.Car
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.lang.RuntimeException

interface CarRepository {
    fun save(car: Car): Long
    fun findById(id: Long): Car?
    fun findBySerialNumber(serialNo: String): Car?
    fun findAll(): List<Car>
}

class CarNotFoundException(message: String) : ResponseStatusException(HttpStatus.NOT_FOUND, message)
class CarCheckUpNotFoundException(message: String) : ResponseStatusException(HttpStatus.NOT_FOUND, message)