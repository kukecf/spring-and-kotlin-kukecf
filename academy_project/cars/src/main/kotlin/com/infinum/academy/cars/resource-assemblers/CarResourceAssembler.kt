package com.infinum.academy.cars.`resource-assemblers`

import com.infinum.academy.cars.controllers.CarCheckUpController
import com.infinum.academy.cars.controllers.CarController
import com.infinum.academy.cars.controllers.CheckUpController
import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
import com.infinum.academy.cars.resource.CarResource
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class CarResourceAssembler : RepresentationModelAssemblerSupport<Car, CarResource>(
    CarController::class.java, CarResource::class.java
) {
    override fun toModel(entity: Car): CarResource {
        return createModelWithId(entity.id, entity).apply {
            add(
                linkTo<CarCheckUpController> {
                    allCheckUpsForCar(
                        entity.id,
                        Pageable.unpaged(),
                        PagedResourcesAssembler<CarCheckUp>(null, null)
                    )
                }.withRel("checkups")
            )
        }
    }

    override fun instantiateModel(entity: Car): CarResource {
        return CarResource(entity)
    }

}