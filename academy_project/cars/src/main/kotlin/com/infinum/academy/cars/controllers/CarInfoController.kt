package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.controllers.assemblers.CarInfoResourceAssembler
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.resource.CarInfoResource
import com.infinum.academy.cars.services.CarInfoAdministrationService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/models")
class CarInfoController(
    private val carInfoService: CarInfoAdministrationService,
    private val resourceAssembler: CarInfoResourceAssembler
) {
    @GetMapping
    fun getAvailableModels(
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarInfo>
    ): ResponseEntity<PagedModel<CarInfoResource>> {
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                carInfoService.getAllModelsInShop(pageable),
                resourceAssembler
            )
        )
    }
}