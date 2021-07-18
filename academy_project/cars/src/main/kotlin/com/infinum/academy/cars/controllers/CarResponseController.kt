package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.repository.Car
import com.infinum.academy.cars.repository.CarCheckUp
import com.infinum.academy.cars.services.CarService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller //
class CarResponseController(
    private val service: CarService
) {
    @RequestMapping("/",produces=[MediaType.TEXT_PLAIN_VALUE])
    fun start(model: Model): ResponseEntity<String> =
        ResponseEntity.ok("Welcome to my autobody shop! :)")

    @GetMapping("/details/{id}",produces=[MediaType.APPLICATION_JSON_VALUE])
    fun details(@PathVariable id: Long): ResponseEntity<Car> {
        return ResponseEntity.ok(service.getCarDetails(id))
    }

    @GetMapping("/checkups/{carId}", produces=[MediaType.APPLICATION_JSON_VALUE])
    fun allCheckUpsForCar(@PathVariable carId: Long): ResponseEntity<List<CarCheckUp>> {
        return ResponseEntity.ok(service.getCarDetails(carId).checkUps)
    }

    @GetMapping("/carCheckUp/{checkUpId}",produces=[MediaType.APPLICATION_JSON_VALUE])
    fun carForCheckUp(@PathVariable checkUpId: Long): ResponseEntity<Car> {
        val checkUp = service.getCarCheckUp(checkUpId)
        return ResponseEntity.ok(service.getCar(checkUp.carId))
    }
}