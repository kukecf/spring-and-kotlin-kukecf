package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.services.CarCheckUpService
import com.infinum.academy.cars.services.CarService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/delete")
class DeletionController(
    private val carService: CarService,
    private val checkUpService: CarCheckUpService
) {
    @PostMapping("/car/{id}")
    fun deleteCar(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(carService.deleteCar(id))
    }

    @PostMapping("/checkup/{id}")
    fun deleteCheckup(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(checkUpService.deleteCheckup(id))
    }
}
