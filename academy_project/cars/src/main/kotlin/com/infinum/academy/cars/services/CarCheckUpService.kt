package com.infinum.academy.cars.services

import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.dto.*
import com.infinum.academy.cars.exceptions.CarCheckUpNotFoundException
import com.infinum.academy.cars.exceptions.CarNotFoundException
import com.infinum.academy.cars.repository.CarCheckUpRepository
import com.infinum.academy.cars.repository.CarRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

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

    fun getAllCheckupsDesc(): List<CarCheckUp> {
        return checkUpRepo.findAllByOrderByDatePerformedDesc()
    }

    fun getLatestCheckups(limit: Int): List<CarCheckUp> {
        return checkUpRepo.findLatestCheckups(limit)
    }

    fun getAllCheckUpsForCarId(id: Long, pageable: Pageable): Page<CarCheckUp> =
        checkUpRepo.findAllByCar(
            carRepo.findById(id) ?: throw CarNotFoundException(id),
            pageable
        )//.map { CheckUpResource(it) }

    fun makeAppointment(addSchedCheckUpDto: AddSchedCheckUpDto) {
        checkUpRepo.save(addSchedCheckUpDto.toCarCheckUp { id ->
            carRepo.findById(id) ?: throw CarNotFoundException(id)
        })
    }

    fun getCheckUp(id: Long): CarCheckUp {
        return checkUpRepo.findById(id) ?: throw CarCheckUpNotFoundException(id)
    }

    fun getUpcomingCheckupsInInterval(duration: Duration, pageable: Pageable): Page<CarCheckUp> {
        return when(duration){
            Duration.ONE_WEEK -> checkUpRepo.findUpcomingWithinWeeks(1, pageable)
            Duration.ONE_MONTH -> checkUpRepo.findUpcomingWithinMonths(1, pageable)
            Duration.SIX_MONTHS -> checkUpRepo.findUpcomingWithinMonths(6, pageable)
        }
    }

}