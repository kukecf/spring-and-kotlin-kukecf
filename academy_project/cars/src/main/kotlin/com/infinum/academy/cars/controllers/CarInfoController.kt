package com.infinum.academy.cars.controllers

import com.infinum.academy.cars.controllers.assemblers.CarInfoPKResourceAssembler
import com.infinum.academy.cars.controllers.assemblers.CarInfoResourceAssembler
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.resource.CarInfoPKResource
import com.infinum.academy.cars.resource.CarInfoResource
import com.infinum.academy.cars.services.CarInfoAdministrationService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/models")
class CarInfoController(
    private val carInfoService: CarInfoAdministrationService,
    private val pkResourceAssembler: CarInfoPKResourceAssembler,
    private val resourceAssembler: CarInfoResourceAssembler
) {
    @GetMapping
    fun getAvailableModels(
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarInfo>
    ): ResponseEntity<PagedModel<CarInfoPKResource>> {
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                carInfoService.getAllModelsInShop(pageable),
                pkResourceAssembler
            )
        )
    }

    @GetMapping("/{manufacturer}-{model}")
    fun getModelWithId(
        @PathVariable manufacturer: String,
        @PathVariable model: String
    ): ResponseEntity<CarInfoResource> {
        return ResponseEntity.ok(
            resourceAssembler.toModel(
                carInfoService.getModelWithId(manufacturer, model)
            )
        )
    }
}
