package com.infinum.academy.cars.controllers.assemblers

import com.infinum.academy.cars.controllers.CarController
import com.infinum.academy.cars.controllers.CarInfoController
import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.resource.CarInfoPKResource
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class CarInfoPKResourceAssembler : RepresentationModelAssemblerSupport<CarInfo, CarInfoPKResource>(
    CarInfoController::class.java, CarInfoPKResource::class.java
) {
    override fun toModel(entity: CarInfo): CarInfoPKResource {
        entity.carInfoPk.let {
            return createModelWithId("${it.manufacturer}-${it.modelName}", entity)
        }
    }

    override fun instantiateModel(entity: CarInfo): CarInfoPKResource {
        return CarInfoPKResource(entity)
    }

}