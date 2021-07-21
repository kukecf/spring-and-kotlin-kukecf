package com.infinum.academy.cars.repository

import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository

@Repository
@Qualifier("in-memory")
class InMemoryCarRepository : CarRepository {
    private val cars = mutableMapOf<Long, Car>()

    override fun save(car: Car): Long {
        val carId = (cars.keys.maxOrNull() ?: 0) + 1
        cars[carId] = car
        return carId
    }

    override fun findById(id: Long): Car? = cars[id]

    fun findBySerialNumber(serialNo: String): Car? {
        return cars.values.find{
            serialNo == it.serialNumber
        }
    }

    override fun findAll(): List<Car> {
        return cars.values.toList()
    }

}