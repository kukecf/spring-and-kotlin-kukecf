package com.infinum.academy.cars.repository

import com.infinum.academy.cars.domain.Car
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

//@org.springframework.stereotype.Repository
interface CarRepository : Repository<Car, Long> {
    fun save(car: Car): Long
    fun findById(id: Long): Car?
    fun findBySerialNumber(serialNo: String): Car?
    fun findAll(): List<Car>
    fun findAll(pageAble: Pageable): Page<Car>

    @Transactional
    fun deleteAll()
}

class CarNotFoundException(message: String) : ResponseStatusException(HttpStatus.NOT_FOUND, message)
class CarCheckUpNotFoundException(message: String) : ResponseStatusException(HttpStatus.NOT_FOUND, message)