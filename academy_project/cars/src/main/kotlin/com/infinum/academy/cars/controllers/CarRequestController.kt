package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.repository.*
import com.infinum.academy.cars.services.CarService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDate
import java.time.LocalDateTime

@Controller
class CarRequestController(
    private val service: CarService
) {
    @PostMapping("/addCar")
    fun addNewCar(@RequestBody carDto: CarDto): ResponseEntity<String> {
        if (service.hasCarWithSerialNumber(carDto.serialNumber)) {
            val failMessage = "There already exists a car with serial number ${carDto.serialNumber} in the repo!"
            //throw CarNotFoundException(failMessage)
            return ResponseEntity(failMessage, HttpStatus.BAD_REQUEST) // mislim da je ovo bolje nego baciti iznimku?
        }
        val car = Car(
            carDto.ownerId,
            LocalDate.now(),
            carDto.manufacturerName,
            carDto.modelName,
            carDto.productionYear,
            carDto.serialNumber,
            mutableListOf<CarCheckUp>()
        )
        service.addCar(car)
        val message = "added this car: $car"
        return ResponseEntity.ok(message)
    }

    @PostMapping("/addCarCheckUp")
    fun addNewCarCheckUp(@RequestBody checkUpDto: CarCheckUpDto): ResponseEntity<String> {
        if (!service.hasCarWithId(checkUpDto.carId)) {
            val failMessage = "No car with ID ${checkUpDto.carId} exists in the repo!"
            //throw CarNotFoundException(failMessage)
            return ResponseEntity(failMessage, HttpStatus.BAD_REQUEST)
        }
        val checkUp = CarCheckUp(
            LocalDateTime.now(),
            checkUpDto.workerName,
            checkUpDto.price,
            checkUpDto.carId
        )
        service.addCarCheckUp(checkUp)
        val message = "added this checkup: $checkUp"
        return ResponseEntity.ok(message)
    }
}