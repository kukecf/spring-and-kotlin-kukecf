package com.infinum.academy.cars.services

import com.infinum.academy.cars.repository.CarCheckUpNotFoundException
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarRepository
import com.infinum.academy.cars.resource.CarCheckUp
import com.infinum.academy.cars.resource.CarCheckUpDto
import com.infinum.academy.cars.resource.toDomainModel
import org.springframework.stereotype.Service

@Service
class CarCheckUpService(
    private val carRepo: CarRepository,
    private val checkUpRepo: CarCheckUpRepository
) {
    fun addCarCheckUp(checkUpDto: CarCheckUpDto): Long {
        val checkUp = checkUpDto.toDomainModel()
        return checkUpRepo.save(checkUp)
    }

    fun getCarCheckUp(checkUpId: Long): CarCheckUp =
        checkUpRepo.findById(checkUpId)
            ?: throw CarCheckUpNotFoundException("Checkup with ID $checkUpId does not exist!")

    fun getAllCheckUps(): List<CarCheckUp> = checkUpRepo.findAll()

    fun getAllCheckUpsForCarId(id: Long): List<CarCheckUp> =
        checkUpRepo.findAllByCarId(id)

}