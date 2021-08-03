package com.infinum.academy.cars.services

import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.dto.AddCarCheckUpDto
import com.infinum.academy.cars.dto.Duration
import com.infinum.academy.cars.dto.toCarCheckUp
import com.infinum.academy.cars.exceptions.CarCheckUpNotFoundException
import com.infinum.academy.cars.exceptions.CarNotFoundException
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CarCheckUpService(
    private val checkUpRepo: CarCheckUpRepository,
    private val carRepo: CarRepository
) {
    fun addCarCheckUp(checkUpDto: AddCarCheckUpDto): Long {
        val checkUp = checkUpDto.toCarCheckUp { carId ->
            carRepo.findById(carId)
                ?: throw CarNotFoundException(carId)
        }
        return checkUpRepo.save(checkUp).id
    }

    fun getLatestCheckups(): List<CarCheckUp> {
        return checkUpRepo.findAllByOrderByDatePerformedDesc().take(10)
    }

    fun getAllCheckUpsForCarId(id: Long, pageable: Pageable): Page<CarCheckUp> =
        checkUpRepo.findAllByCarId(
            id,
            pageable
        )

    fun getCheckUp(id: Long): CarCheckUp {
        return checkUpRepo.findById(id) ?: throw CarCheckUpNotFoundException(id)
    }

    fun getUpcomingCheckupsInInterval(duration: Duration, pageable: Pageable): Page<CarCheckUp> {
        return checkUpRepo.findByDatePerformedBetween(
            LocalDateTime.now(),
            LocalDateTime.now().plus(duration.toPeriod()),
            pageable
        )
    }

}