package com.infinum.academy.cars.repository

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.transaction.annotation.Transactional


interface CarCheckUpRepository : Repository<CarCheckUp, Long> {
    fun save(checkup: CarCheckUp): CarCheckUp
    fun saveAll(checkups: Iterable<CarCheckUp>): Iterable<CarCheckUp>
    fun findById(id: Long): CarCheckUp?
    fun findAll(): List<CarCheckUp>

    @Query("select check from CarCheckUp check join fetch Car car on car.id=check.car.id where check.car.id = :carId order by check.datePerformed desc")
    fun findAllCheckupsForDetails(carId: Long): List<CarCheckUp>

    fun findAllByCar(car: Car, pageable: Pageable): Page<CarCheckUp>

    fun deleteAll()
}