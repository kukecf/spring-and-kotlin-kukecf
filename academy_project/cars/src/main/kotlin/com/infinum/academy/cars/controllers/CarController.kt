package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.dto.CarDetailsDto
import com.infinum.academy.cars.dto.CarDto
import com.infinum.academy.cars.services.CarService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    @GetMapping
    fun getAllCars(pageable: Pageable): ResponseEntity<Page<Car>> =
        ResponseEntity.ok(service.getAllCars(pageable))

    @PostMapping
    fun addNewCar(@RequestBody carDto: CarDto): ResponseEntity<Unit> {
        val id = service.addCar(carDto)
        return ResponseEntity.created(URI("http://localhost:8080/cars/$id")).build()
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun details(@PathVariable id: Long): ResponseEntity<CarDetailsDto> {
        return ResponseEntity.ok(service.getCarDetails(id))
    }

}