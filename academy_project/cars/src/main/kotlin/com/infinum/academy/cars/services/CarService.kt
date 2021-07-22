package com.infinum.academy.cars.services

import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarNotFoundException
import com.infinum.academy.cars.repository.CarRepository
import com.infinum.academy.cars.resource.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CarService(
    @Qualifier("db") private val carRepo: CarRepository,
    @Qualifier("db") private val checkUpRepo: CarCheckUpRepository
) {
    fun addCar(carDto: CarDto): Long {
        val car = carDto.toDomainModel()
        return carRepo.save(car)
    }

    private fun getCar(carId: Long): Car =
        carRepo.findById(carId) ?: throw CarNotFoundException("Car with ID $carId does not exist!")

    fun getCarDetails(carId: Long): Car {
        return getCar(carId).copy(checkUps=checkUpRepo.findAllByCarId(carId))
    }

    fun getCarBySerial(serNo: String): Car =
        carRepo.findBySerialNumber(serNo) ?: throw CarNotFoundException("Car with serial number $serNo does not exist!")

    fun getAllCars(): List<Car> =
        carRepo.findAll()
}

class CarConflictException(message: String) : ResponseStatusException(HttpStatus.CONFLICT, message)
class CarCheckUpConflictException(message: String) : ResponseStatusException(HttpStatus.CONFLICT, message)
