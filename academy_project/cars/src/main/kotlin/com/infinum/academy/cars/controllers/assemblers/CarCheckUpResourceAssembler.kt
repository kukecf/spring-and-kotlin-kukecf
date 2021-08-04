package com.infinum.academy.cars.controllers.assemblers

import com.infinum.academy.cars.controllers.CarController
import com.infinum.academy.cars.controllers.CheckUpController
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.resource.CheckUpResource
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class CarCheckUpResourceAssembler : RepresentationModelAssemblerSupport<CarCheckUp, CheckUpResource>(
    CheckUpController::class.java, CheckUpResource::class.java
) {
    override fun toModel(entity: CarCheckUp): CheckUpResource {
        return createModelWithId(entity.id, entity).apply{
            add(
                linkTo<CarController> {
                    details(entity.car.id)
                }.withRel("car")
            )
        }
    }

    override fun instantiateModel(entity: CarCheckUp): CheckUpResource {
        return CheckUpResource(entity)
    }

}