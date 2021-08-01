package com.infinum.academy.cars.resource

import com.fasterxml.jackson.annotation.JsonInclude
import com.infinum.academy.cars.domain.Car
import com.infinum.academy.cars.domain.CarCheckUp
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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val checkups: List<CheckUpResource> = emptyList()
) : RepresentationModel<CarResource>() {
    constructor(car: Car, checkups: List<CarCheckUp>) : this(
        car.id,
        car.owner_id,
        car.date_added,
        car.info.carInfoPk.manufacturer,
        car.info.carInfoPk.modelName,
        car.production_year,
        car.serial_number,
        checkups.map { CheckUpResource(it) }
    )

    constructor(car: Car) : this(car, emptyList())
}
