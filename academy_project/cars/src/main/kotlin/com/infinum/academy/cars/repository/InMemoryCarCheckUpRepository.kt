package com.infinum.academy.cars.repository

import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository

@Repository
@Qualifier("in-memory")
class InMemoryCarCheckUpRepository : CarCheckUpRepository{
    private val carCheckUps = mutableMapOf<Long, CarCheckUp>()

    override fun save(checkUp: CarCheckUp): Long {
        val id = checkUp.checkUpId
        carCheckUps[id] = checkUp
        return id
    }

    override fun findById(id: Long): CarCheckUp? = carCheckUps[id]

    override fun findAll(): List<CarCheckUp> {
        return carCheckUps.values.toList()
    }
}