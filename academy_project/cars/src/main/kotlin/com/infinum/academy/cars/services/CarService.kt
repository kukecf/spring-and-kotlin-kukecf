package com.infinum.academy.cars.services

import com.infinum.academy.cars.repository.CarCheckUpNotFoundException
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarNotFoundException
import com.infinum.academy.cars.repository.CarRepository
import com.infinum.academy.cars.resource.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class CarService(
    @Qualifier("db") private val carRepo: CarRepository,
    @Qualifier("db") private val checkUpRepo: CarCheckUpRepository
) {
    @Transactional
    fun addCar(carDto: CarDto): Long {
        val id = (carRepo.findAll().maxOfOrNull { car -> car.carId } ?: 0) + 1
        val car = carDto.toDomainModel(id)
        return carRepo.save(car)
    }

    @Transactional
    fun addCarCheckUp(checkUpDto: CarCheckUpDto): Long {
        val id = (checkUpRepo.findAll().maxOfOrNull { checkup -> checkup.checkUpId } ?: 0) + 1
        val checkUp = checkUpDto.toDomainModel(id)
        if (carIdExists(checkUp.carId).not()) {
            throw CarCheckUpConflictException("Failed to add checkup $checkUp to car with ID ${checkUp.carId}")
        }
        return checkUpRepo.save(checkUp)
    }

    fun getCarCheckUp(checkUpId: Long): CarCheckUp =
        checkUpRepo.findById(checkUpId)
            ?: throw CarCheckUpNotFoundException("Checkup with ID $checkUpId does not exist!")

    fun getCar(carId: Long): Car =
        carRepo.findById(carId) ?: throw CarNotFoundException("Car with ID $carId does not exist!")

    fun getCarDetails(carId: Long): CarDetails {
        return CarDetails(
            getCar(carId),
            checkUpRepo.findAll().filter { checkUp ->
                checkUp.carId == carId
            }.sortedByDescending { it.datePerformed }
        )
    }

    fun getAllCars(): List<Car> = carRepo.findAll()

    fun getAllCheckUps(): List<CarCheckUp> = checkUpRepo.findAll()

    private fun carIdExists(id: Long): Boolean =
        carRepo.findAll()
            .any {
                it.carId == id
            }

}

data class CarDetails(
    val car: Car,
    val checkUps: List<CarCheckUp>
)

class CarConflictException(message: String) : ResponseStatusException(HttpStatus.CONFLICT, message)
class CarCheckUpConflictException(message: String) : ResponseStatusException(HttpStatus.CONFLICT, message)
