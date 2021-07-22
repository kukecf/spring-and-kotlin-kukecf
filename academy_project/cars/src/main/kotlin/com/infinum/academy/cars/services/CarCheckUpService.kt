package com.infinum.academy.cars.services

import com.infinum.academy.cars.repository.CarCheckUpNotFoundException
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarRepository
import com.infinum.academy.cars.resource.CarCheckUp
import com.infinum.academy.cars.resource.CarCheckUpDto
import com.infinum.academy.cars.resource.toDomainModel
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class CarCheckUpService(
    @Qualifier("db") private val carRepo: CarRepository,
    @Qualifier("db") private val checkUpRepo: CarCheckUpRepository
) {
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

    fun getAllCheckUps(): List<CarCheckUp> = checkUpRepo.findAll()

    fun getAllCheckUpsForCarId(id:Long):List<CarCheckUp> =
        checkUpRepo.findAllByCarId(id)


    private fun carIdExists(id: Long): Boolean =
        carRepo.findAll()
            .any {
                it.carId == id
            }

}