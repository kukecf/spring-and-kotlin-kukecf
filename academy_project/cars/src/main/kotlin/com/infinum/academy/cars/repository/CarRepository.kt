package com.infinum.academy.cars.repository

import com.infinum.academy.cars.domain.Car
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository

interface CarRepository : Repository<Car, Long> {

    fun save(car: Car): Car

    fun saveAll(checkups: Iterable<Car>): Iterable<Car>

    fun findById(id: Long): Car?

    fun findAll(pageable: Pageable): Page<Car>

    fun findAll(): List<Car>

    fun deleteAll()
}
