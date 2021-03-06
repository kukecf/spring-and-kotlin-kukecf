package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.controllers.assemblers.CarModelInfoResourceAssembler
import com.infinum.academy.cars.controllers.assemblers.CarResourceAssembler
import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.dto.AddCarDto
import com.infinum.academy.cars.resource.CarModelInfoResource
import com.infinum.academy.cars.resource.CarResource
import com.infinum.academy.cars.services.CarService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Controller
@RequestMapping("/cars")
class CarController(
    private val service: CarService,
    private val resourceAssembler: CarResourceAssembler,
    private val carModelInfoResourceAssembler: CarModelInfoResourceAssembler
) {

    @GetMapping
    fun getAllCars(
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<Car>
    ): ResponseEntity<PagedModel<CarResource>> {
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                service.getAllCars(pageable),
                resourceAssembler
            )
        )
    }

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

    @GetMapping("/{id}")
    fun details(@PathVariable id: Long): ResponseEntity<CarResource> {
        return ResponseEntity.ok(resourceAssembler.toModel(service.getCar(id)))
    }

    @DeleteMapping("/{id}")
    fun deleteCar(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(service.deleteCar(id))
    }

    @GetMapping("/models")
    fun getAvailableModels(
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarInfo>
    ): ResponseEntity<PagedModel<CarModelInfoResource>> {
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                service.getAllAvailableModels(pageable),
                carModelInfoResourceAssembler
            )
        )
    }
}
