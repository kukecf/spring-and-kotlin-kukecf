package com.infinum.academy.cars.repository

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository
import org.springframework.transaction.annotation.Transactional


interface CarCheckUpRepository : Repository<CarCheckUp, Long> {
    fun save(checkup: CarCheckUp): Long
    fun findById(id: Long): CarCheckUp?
    fun findAllByCarId(carId: Long): List<CarCheckUp>
    fun findAllByCarId(carId: Long, pageable:Pageable): Page<CarCheckUp>
    fun findAll(): List<CarCheckUp>
    fun findAll(pageable:Pageable) : Page<CarCheckUp>
    @Transactional
    fun deleteAll()
}