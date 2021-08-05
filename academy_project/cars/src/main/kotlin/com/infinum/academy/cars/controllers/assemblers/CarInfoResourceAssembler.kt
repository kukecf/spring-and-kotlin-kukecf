package com.infinum.academy.cars.controllers.assemblers

import com.infinum.academy.cars.controllers.CarController
import com.infinum.academy.cars.controllers.CarInfoController
import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.resource.CarInfoResource
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class CarInfoResourceAssembler : RepresentationModelAssemblerSupport<CarInfo, CarInfoResource>(
    CarInfoController::class.java, CarInfoResource::class.java
) {
    override fun toModel(entity: CarInfo): CarInfoResource {
        entity.carInfoPk.let {
            return createModelWithId("${it.manufacturer}-${it.modelName}", entity).apply {
                add(
                    linkTo<CarController> {
                        allCarsWithModelInfo(
                            it.manufacturer,
                            it.modelName,
                            Pageable.unpaged(),
                            PagedResourcesAssembler<Car>(null, null)
                        )
                    }.withRel("cars")
                )
            }
        }
    }

    override fun instantiateModel(entity: CarInfo): CarInfoResource {
        return CarInfoResource(entity)
    }

}