package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.resource.Car
import com.infinum.academy.cars.resource.CarDto
import com.infinum.academy.cars.services.CarService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/cars")
class CarController(
    private val service: CarService
) {
    @GetMapping("/", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllCars(): ResponseEntity<List<Car>> =
        ResponseEntity.ok(service.getAllCars())

    @PostMapping("/add", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addNewCar(@RequestBody carDto: CarDto): ResponseEntity<String> {
        val headers = HttpHeaders()
        val id = service.addCar(carDto)
        headers.add("Location", "http://localhost:8080/cars/$id")
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun details(@PathVariable id: Long): ResponseEntity<Car> {
        return ResponseEntity.ok(service.getCarDetails(id))
    }

}