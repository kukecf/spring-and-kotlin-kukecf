package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarCheckUp
import com.infinum.academy.cars.resource.CarCheckUpDto
import com.infinum.academy.cars.services.CarCheckUpService
import com.infinum.academy.cars.services.CarService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URI

@Controller
@RequestMapping("/checkups")
class CarCheckUpController(
    private val service: CarCheckUpService
) {
    @GetMapping
    fun getAllCarCheckUps(): ResponseEntity<List<CarCheckUp>> =
        ResponseEntity.ok(service.getAllCheckUps())

    @PostMapping
    fun addNewCarCheckUp(@RequestBody checkUpDto: CarCheckUpDto): ResponseEntity<Unit> {
        val id = service.addCarCheckUp(checkUpDto)
        return ResponseEntity.created(URI("http://localhost:8080/checkups/created/$id")).build()
    }

    @GetMapping("/{carId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allCheckUpsForCar(@PathVariable carId: Long): ResponseEntity<List<CarCheckUp>> =
        ResponseEntity.ok(service.getAllCheckUpsForCarId(carId))
}