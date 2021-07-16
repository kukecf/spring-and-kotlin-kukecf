package com.infinum.academy.cars.repository

import org.springframework.stereotype.Component

//pokusao sam maksimalno rasteretiti ovu klasu da se moze prirodno umetnuti baza kao repozitorij na njezino mjesto
//jedino bi mozda bilo idealno kada bi indeksiranje nekako razdvojili (ili opet previse kompliciram?)
@Component
class InMemoryBodyShopRepository : BodyShopRepository {
    private val cars = mutableMapOf<Long, Car>()
    private val carCheckUps = mutableMapOf<Long, CarCheckUp>()

    override fun insertCar(car: Car): Long {
        val carId = (cars.keys.maxOrNull() ?: 0) + 1
        cars[carId] = car
        return carId
    }

    override fun registerCheckUp(checkUp: CarCheckUp): Long {
        val checkUpId = (carCheckUps.keys.maxOrNull() ?: 0) + 1
        carCheckUps[checkUpId] = checkUp
        cars[checkUp.carId]?.checkUps?.add(checkUp) //nisam zadovoljan s ovim
            ?: throw RuntimeException("Failed to add checkup $checkUp to car with ID ${checkUp.carId}")
        return checkUpId
    }

    override fun getCar(id: Long): Car =
        cars[id] ?: throw CarNotFoundException("No car with ID $id exists.")

    override fun getCarCheckUp(id: Long): CarCheckUp =
        carCheckUps[id] ?: throw CarCheckUpNotFoundException("No check up with ID $id exists.")

    override fun hasCarWithId(id: Long): Boolean {
        return cars.containsKey(id)
    }

    override fun hasCarWithSerialNumber(serialNo: String): Boolean {
        return cars.values.any {
            it.serialNumber == serialNo
        }
    }

}