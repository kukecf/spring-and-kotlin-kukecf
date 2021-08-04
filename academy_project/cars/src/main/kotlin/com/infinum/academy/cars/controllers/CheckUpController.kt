package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.controllers.assemblers.CarCheckUpResourceAssembler
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.dto.AddCarCheckUpDto
import com.infinum.academy.cars.dto.Duration
import com.infinum.academy.cars.resource.CheckUpResource
import com.infinum.academy.cars.services.CarCheckUpService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Controller
@RequestMapping("/checkups")
class CheckUpController(
    private val service: CarCheckUpService,
    private val resourceAssembler: CarCheckUpResourceAssembler
) {
    @PostMapping
    fun addCarCheckUp(@RequestBody checkUpDto: AddCarCheckUpDto): ResponseEntity<Unit> {
        val id = service.addCarCheckUp(checkUpDto)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping("/latest")
    fun latestCheckups(
        @RequestParam(defaultValue = "10") limit: Int,
        pagedResourcesAssembler : PagedResourcesAssembler<CarCheckUp>
    ): ResponseEntity<PagedModel<CheckUpResource>> {
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                service.getLatestCheckups(Pageable.ofSize(limit)),
                resourceAssembler
            )
        )
    }

    @GetMapping("/{id}")
    fun getCheckup(@PathVariable id: Long): ResponseEntity<CheckUpResource> {
        return ResponseEntity.ok(
            resourceAssembler.toModel(
                service.getCheckUp(id)
            )
        )
    }

    @GetMapping("/upcoming")
    fun getUpcomingCheckupAppointments(
        @RequestParam(defaultValue = "ONE_MONTH") duration: Duration,
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarCheckUp>
    ): ResponseEntity<PagedModel<CheckUpResource>> {
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                service.getUpcomingCheckupsInInterval(duration, pageable),
                resourceAssembler
            )
        )
    }

}