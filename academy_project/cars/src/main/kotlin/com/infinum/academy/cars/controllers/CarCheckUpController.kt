package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.`resource-assemblers`.CarCheckUpResourceAssembler
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.resource.CheckUpResource
import com.infinum.academy.cars.services.CarCheckUpService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cars/{carId}/checkups")
class CarCheckUpController(
    private val service: CarCheckUpService,
    private val resourceAssembler: CarCheckUpResourceAssembler
) {
    @GetMapping
    fun allCheckUpsForCar(
        @PathVariable carId: Long,
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarCheckUp>
    ): ResponseEntity<PagedModel<CheckUpResource>> =
        ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                service.getAllCheckUpsForCarId(carId, pageable),
                resourceAssembler
            )
        )
}