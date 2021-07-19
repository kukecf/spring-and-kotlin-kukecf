package com.infinum.academy.cars.repository

import com.infinum.academy.cars.resource.CarCheckUp

interface CarCheckUpRepository {
    fun save(checkup: CarCheckUp): Long
    fun findById(id: Long): CarCheckUp?
    fun findAll(): List<CarCheckUp>
}