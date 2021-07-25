package com.infinum.academy.cars.services

import com.infinum.academy.cars.repository.CarCheckUpNotFoundException
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.dto.CarCheckUpDto
import com.infinum.academy.cars.dto.toDomainModel
import com.infinum.academy.cars.repository.CarRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CarCheckUpService(
    private val checkUpRepo: CarCheckUpRepository,
    private val carRepo: CarRepository
) {
    fun addCarCheckUp(checkUpDto: CarCheckUpDto): Long {
        //val car = carRepo.findById(checkUpDto.carId) ?: throw CarNotFoundException("Car with ID ${checkUpDto.carId} does not exist!")
        val checkUp = checkUpDto.toDomainModel()
        return checkUpRepo.save(checkUp).id
    }

    fun getCarCheckUp(checkUpId: Long): CarCheckUp =
        checkUpRepo.findById(checkUpId)
            ?: throw CarCheckUpNotFoundException("Checkup with ID $checkUpId does not exist!")

    fun getAllCheckUps(pageable: Pageable): Page<CarCheckUp> = checkUpRepo.findAll(pageable)

    fun getAllCheckUpsForCarId(id: Long): List<CarCheckUp> =
        checkUpRepo.findAllCheckupsForDetails(id)

    fun getAllCheckUpsForCarId(id: Long, pageable: Pageable): Page<CarCheckUp> =
        checkUpRepo.findAllByCarId(id, pageable)

}