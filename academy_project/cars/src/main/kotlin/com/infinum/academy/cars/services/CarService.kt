package com.infinum.academy.cars.services

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarInfoPrimaryKey
import com.infinum.academy.cars.dto.AddCarDto
import com.infinum.academy.cars.dto.toCar
import com.infinum.academy.cars.exceptions.CarInfoNotFoundException
import com.infinum.academy.cars.exceptions.CarNotFoundException
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarInfoRepository
import com.infinum.academy.cars.repository.CarRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CarService(
    private val carRepo: CarRepository,
    private val checkUpRepo: CarCheckUpRepository,
    private val carInfoRepo: CarInfoRepository
) {
    fun addCar(carDto: AddCarDto): Long {
        val car = carDto.toCar { man, model ->
            carInfoRepo.findByCarInfoPk(
                CarInfoPrimaryKey(
                    man, model
                )
            )
                ?: throw CarInfoNotFoundException(man, model)
        }
        return carRepo.save(car).id
    }

    fun getCar(carId: Long): Car =
        carRepo.findById(carId) ?: throw CarNotFoundException(carId)

    fun getAllCars(pageable: Pageable): Page<Car> {
        return carRepo.findAll(pageable)
    }
}
