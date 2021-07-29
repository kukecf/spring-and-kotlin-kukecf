package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.dto.CarInfoDto
import com.infinum.academy.cars.services.CarInfoService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class CarInfoController(
    private val service: CarInfoService
) {
    @GetMapping("/infotest")
    fun getCars(): ResponseEntity<List<CarInfoDto>> {
        return ResponseEntity.ok(service.getModelsFromServer())
    }
}