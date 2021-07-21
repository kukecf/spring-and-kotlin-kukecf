package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarDto
import com.infinum.academy.cars.services.CarDetails
import com.infinum.academy.cars.services.CarService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URI

@Controller
@RequestMapping("/cars")
class CarController(
    private val service: CarService
) {
    @GetMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllCars(): ResponseEntity<List<Car>> =
        ResponseEntity.ok(service.getAllCars())

    @PostMapping
    fun addNewCar(@RequestBody carDto: CarDto): ResponseEntity<String> {
        val id = service.addCar(carDto)
        return ResponseEntity.created(URI("http://localhost:8080/cars/$id")).build()
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun details(@PathVariable id: Long): ResponseEntity<CarDetails> {
        return ResponseEntity.ok(service.getCarDetails(id))
    }

}