package com.infinum.academy.cars.`resource-assemblers`

import com.infinum.academy.cars.controllers.CheckUpController
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.resource.CheckUpResource
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.stereotype.Component

@Component
class CarCheckUpResourceAssembler : RepresentationModelAssemblerSupport<CarCheckUp, CheckUpResource>(
    CheckUpController::class.java, CheckUpResource::class.java
) {
    override fun toModel(entity: CarCheckUp): CheckUpResource {
        return createModelWithId(entity.id, entity)
    }

    override fun instantiateModel(entity: CarCheckUp): CheckUpResource {
        return CheckUpResource(entity)
    }

}