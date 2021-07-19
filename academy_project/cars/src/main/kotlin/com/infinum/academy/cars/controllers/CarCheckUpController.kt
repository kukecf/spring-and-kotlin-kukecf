package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import com.infinum.academy.cars.resource.CarCheckUpDto
import com.infinum.academy.cars.services.CarService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/checkups")
class CarCheckUpController(
    private val service: CarService
) {
    @GetMapping("/",consumes=[MediaType.APPLICATION_JSON_VALUE])
    fun getAllCarCheckUps(): ResponseEntity<List<CarCheckUp>> =
        ResponseEntity.ok(service.getAllCheckUps())

    @PostMapping("/add", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addNewCarCheckUp(@RequestBody checkUpDto: CarCheckUpDto): ResponseEntity<String> {
        return try {
            val id = service.addCarCheckUp(checkUpDto)
            val headers=HttpHeaders()
            headers.add("Location", "http://localhost:8080/checkups/$id")
            ResponseEntity(headers,HttpStatus.CREATED)
        } catch (e: ResponseStatusException) {
            throw e
        }
    }

    @GetMapping("/{carId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allCheckUpsForCar(@PathVariable carId: Long): ResponseEntity<List<CarCheckUp>> {
        return try {
            val carDetails = service.getCarDetails(carId)
            ResponseEntity.ok(carDetails.checkUps)
        } catch (e: ResponseStatusException) {
            throw e
        }
    }

    @GetMapping("/car/{checkUpId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun carForCheckUp(@PathVariable checkUpId: Long): ResponseEntity<Car> {
        return try {
            val checkUp = service.getCarCheckUp(checkUpId)
            ResponseEntity.ok(service.getCar(checkUp.carId))
        } catch (e: ResponseStatusException) {
            throw e
        }
    }
}