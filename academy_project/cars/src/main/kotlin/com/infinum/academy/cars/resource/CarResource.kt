package com.infinum.academy.cars.resource

import com.infinum.academy.cars.domain.Car
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDate

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
data class CarResource(
    val id: Long = 0,

    val ownerId: Long,

    val dateAdded: LocalDate,

    val manufacturerName: String,

    val modelName: String,

    val productionYear: Int,

    val serialNumber: String,

    ) : RepresentationModel<CarResource>() {
    constructor(car: Car) : this(
        car.id,
        car.ownerId,
        car.dateAdded,
        car.info.carInfoPk.manufacturer,
        car.info.carInfoPk.modelName,
        car.productionYear,
        car.serialNumber,
    )
}
