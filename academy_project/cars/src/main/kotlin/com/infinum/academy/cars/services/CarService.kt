package com.infinum.academy.cars.services

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.dto.CarDto
import com.infinum.academy.cars.dto.toDomainModel
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarNotFoundException
import com.infinum.academy.cars.repository.CarRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CarService(
    private val carRepo: CarRepository,
    private val checkUpRepo: CarCheckUpRepository
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

    fun getAllCars(pageable: Pageable): Page<Car> =
        carRepo.findAll(pageable)
}
