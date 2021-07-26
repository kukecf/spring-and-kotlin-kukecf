package com.infinum.academy.cars.services

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.dto.CarDetailsDto
import com.infinum.academy.cars.dto.CarDto
import com.infinum.academy.cars.dto.toCarCheckUp
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
        val car = carDto.toCarCheckUp()
        return carRepo.save(car).id
    }

    private fun getCar(carId: Long): Car =
        carRepo.findById(carId) ?: throw CarNotFoundException(carId)

    fun getCarDetails(carId: Long): CarDetailsDto {
        return CarDetailsDto(getCar(carId),checkUpRepo.findAllCheckupsForDetails(carId))
    }

    fun getAllCars(pageable: Pageable): Page<Car> {
       return carRepo.findAll(pageable)
    }
}
