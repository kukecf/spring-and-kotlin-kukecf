package com.infinum.academy.cars.services

import com.infinum.academy.cars.repository.BodyShopRepository
import com.infinum.academy.cars.repository.Car
import com.infinum.academy.cars.repository.CarCheckUp
import org.springframework.stereotype.Service

@Service
class CarService(
    private val repo: BodyShopRepository
) {
    fun addCar(car: Car): Long {
        return repo.insertCar(car)
    }

    fun addCarCheckUp(checkUp: CarCheckUp): Long {
        return repo.registerCheckUp(checkUp)
    }

    fun getCarCheckUp(checkUpId: Long): CarCheckUp {
        return repo.getCarCheckUp(checkUpId)
    }

    fun getCar(carId: Long): Car {
        return repo.getCar(carId)
    }

    fun getCarDetails(carId: Long): Car {
        val car = repo.getCar(carId)
        return car.copy(checkUps = car.checkUps.asReversed()) // jel ovo ok?
    }

    fun hasCarWithId(carId: Long): Boolean {
        return repo.hasCarWithId(carId)
    }

    fun hasCarWithSerialNumber(serialNo: String): Boolean {
        return repo.hasCarWithSerialNumber(serialNo)
    }

}

