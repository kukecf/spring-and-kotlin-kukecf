package com.infinum.academy.cars.controllers.assemblers

import com.infinum.academy.cars.controllers.CarController
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.resource.CarModelInfoResource
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.stereotype.Component

@Component
class CarModelInfoResourceAssembler : RepresentationModelAssemblerSupport<CarInfo, CarModelInfoResource>(
    CarController::class.java, CarModelInfoResource::class.java
) {
    override fun toModel(entity: CarInfo): CarModelInfoResource {
        return instantiateModel(entity)
    }

    override fun instantiateModel(entity: CarInfo): CarModelInfoResource {
        return CarModelInfoResource(entity)
    }
}
