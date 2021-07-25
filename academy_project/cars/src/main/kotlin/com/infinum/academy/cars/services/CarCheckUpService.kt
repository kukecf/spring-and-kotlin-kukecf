package com.infinum.academy.cars.services

import com.infinum.academy.cars.repository.CarCheckUpNotFoundException
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.dto.CarCheckUpDto
import com.infinum.academy.cars.dto.toDomainModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CarCheckUpService(
    private val checkUpRepo: CarCheckUpRepository
) {
    fun addCarCheckUp(checkUpDto: CarCheckUpDto): Long {
        val checkUp = checkUpDto.toDomainModel()
        return checkUpRepo.save(checkUp).id
    }

    fun getCarCheckUp(checkUpId: Long): CarCheckUp =
        checkUpRepo.findById(checkUpId)
            ?: throw CarCheckUpNotFoundException("Checkup with ID $checkUpId does not exist!")

    fun getAllCheckUps(): List<CarCheckUp> = checkUpRepo.findAll()

    fun getAllCheckUps(pageable: Pageable): Page<CarCheckUp> = checkUpRepo.findAll(pageable)

    fun getAllCheckUpsForCarId(id: Long): List<CarCheckUp> =
        checkUpRepo.findAllByCarId(id)

    fun getAllCheckUpsForCarId(id: Long, pageable: Pageable): Page<CarCheckUp> =
        checkUpRepo.findAllByCarId(id, pageable)

}