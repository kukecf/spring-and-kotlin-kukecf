package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.dto.AddCarCheckUpDto
import com.infinum.academy.cars.dto.CheckUpDto
import com.infinum.academy.cars.services.CarCheckUpService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Controller
@RequestMapping("/checkups")
class CarCheckUpController(
    private val service: CarCheckUpService
) {
    @PostMapping
    fun addCarCheckUp(@RequestBody checkUpDto: AddCarCheckUpDto): ResponseEntity<Unit> {
        val id = service.addCarCheckUp(checkUpDto)
        val location=ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .port(8080)
            .buildAndExpand(id)
            .toUri()
        return ResponseEntity.created(location).build()
    }

    //mozda i ovdje vratiti checkUpDetailsDto?
    @GetMapping("/car/{carId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allCheckUpsForCar(@PathVariable carId: Long, pageable:Pageable): ResponseEntity<Page<CheckUpDto>> =
        ResponseEntity.ok(service.getAllCheckUpsForCarId(carId,pageable))
}