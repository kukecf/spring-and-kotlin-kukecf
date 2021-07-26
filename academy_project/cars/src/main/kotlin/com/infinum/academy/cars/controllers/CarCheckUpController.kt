package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.dto.CarCheckUpDto
import com.infinum.academy.cars.services.CarCheckUpService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/checkups")
class CarCheckUpController(
    private val service: CarCheckUpService
) {
    @PostMapping
    fun addNewCarCheckUp(@RequestBody checkUpDto: CarCheckUpDto): ResponseEntity<Unit> {
        val id = service.addCarCheckUp(checkUpDto)
        return ResponseEntity.created(URI("http://localhost:8080/checkups/$id")).build()
    }

    @GetMapping("/car/{carId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun allCheckUpsForCar(@PathVariable carId: Long, pageable:Pageable): ResponseEntity<Page<CarCheckUp>> =
        ResponseEntity.ok(service.getAllCheckUpsForCarId(carId,pageable))
}