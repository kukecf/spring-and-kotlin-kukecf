package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.controllers.assemblers.CarCheckUpResourceAssembler
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.exceptions.CarNotFoundException
import com.infinum.academy.cars.resource.CheckUpResource
import com.infinum.academy.cars.services.CarCheckUpService
import com.infinum.academy.cars.services.CarService
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
    private val checkupService: CarCheckUpService,
    private val resourceAssembler: CarCheckUpResourceAssembler
) {
    @GetMapping
    fun allCheckUpsForCar(
        @PathVariable carId: Long,
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarCheckUp>
    ): ResponseEntity<PagedModel<CheckUpResource>> {
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                checkupService.getAllCheckUpsForCarId(carId, pageable),
                resourceAssembler
            )
        )
    }
}
