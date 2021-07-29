package com.infinum.academy.cars.repository

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

interface CarRepository : Repository<Car, Long> {
    fun save(car: Car): Car
    fun saveAll(checkups: Iterable<Car>): Iterable<Car>
    fun findById(id: Long): Car?

    fun findAll(pageable: Pageable): Page<Car>
    fun findAll(): List<Car>

    fun deleteAll()
}

class CarNotFoundException(id: Long) : ResponseStatusException(HttpStatus.NOT_FOUND, "Car with ID $id does not exist!")
class CarCheckUpNotFoundException(id: Long) : ResponseStatusException(HttpStatus.NOT_FOUND, "Checkup with ID $id does not exist!")