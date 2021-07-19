package com.infinum.academy.cars.services

import com.infinum.academy.cars.repository.CarCheckUpNotFoundException
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarNotFoundException
import com.infinum.academy.cars.repository.CarRepository
import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import com.infinum.academy.cars.resource.CarCheckUpDto
import com.infinum.academy.cars.resource.CarDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CarService(
    private val carRepo: CarRepository,
    private val checkUpRepo: CarCheckUpRepository
) {
    fun addCar(carDto: CarDto): Long {
        val id = (carRepo.findAll().maxOfOrNull { car -> car.carId } ?: 0) + 1
        val car = carDto.toDomainModel(id)
        if (hasCarWithSerialNumber(car.serialNumber))
            throw CarConflictException("Car with serial number ${car.serialNumber} already exists in the repository!")
        return carRepo.save(car)
    }

    fun addCarCheckUp(checkUpDto: CarCheckUpDto): Long {
        val id = (checkUpRepo.findAll().maxOfOrNull { checkup -> checkup.checkUpId } ?: 0) + 1
        val checkUp = checkUpDto.toDomainModel(id)
        if (carIdExists(checkUp.carId).not()) {
            throw CarCheckUpConflictException("Failed to add checkup $checkUp to car with ID ${checkUp.carId}")
        }
        else{
            carRepo.findById(checkUp.carId)?.checkUps?.add(checkUp)
        }
        return checkUpRepo.save(checkUp)
    }

    fun getCarCheckUp(checkUpId: Long): CarCheckUp =
        checkUpRepo.findById(checkUpId)
            ?: throw CarCheckUpNotFoundException("Checkup with ID $checkUpId does not exist!")


    fun getCar(carId: Long): Car =
        carRepo.findById(carId) ?: throw CarNotFoundException("Car with ID $carId does not exist!")

    fun getCarDetails(carId: Long): Car {
        val car = carRepo.findById(carId)
        return car?.copy(checkUps = car.checkUps.asReversed())
            ?: throw CarNotFoundException("Car with ID $carId does not exist!")// jel ovo ok?
    }

    fun getAllCars() : List<Car> = carRepo.findAll()

    fun getAllCheckUps() : List<CarCheckUp> = checkUpRepo.findAll()

    private fun hasCarWithSerialNumber(serialNo: String): Boolean =
        carRepo.findBySerialNumber(serialNo) != null

    private fun carIdExists(id: Long): Boolean =
        carRepo.findAll()
            .any {
                it.carId == id
            }

}

class CarConflictException(message: String) : ResponseStatusException(HttpStatus.CONFLICT, message)
class CarCheckUpConflictException(message: String) : ResponseStatusException(HttpStatus.CONFLICT, message)
