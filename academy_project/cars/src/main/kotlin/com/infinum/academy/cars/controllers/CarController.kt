package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.dto.AddCarDto
import com.infinum.academy.cars.dto.CarDto
import com.infinum.academy.cars.services.CarService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Controller
@RequestMapping("/cars")
class CarController(
    private val service: CarService
) {

    @GetMapping
    fun getAllCars(pageable: Pageable): ResponseEntity<Page<CarDto>> =
        ResponseEntity.ok(service.getAllCars(pageable))

    @PostMapping
    fun addNewCar(@RequestBody carDto: AddCarDto): ResponseEntity<Unit> {
        val id = service.addCar(carDto)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun details(@PathVariable id: Long): ResponseEntity<CarDto> {
        return ResponseEntity.ok(service.getCarDetails(id))
    }

}