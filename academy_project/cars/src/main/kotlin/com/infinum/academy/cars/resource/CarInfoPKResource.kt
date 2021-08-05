package com.infinum.academy.cars.resource

import com.infinum.academy.cars.domain.CarInfo
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
data class CarInfoPKResource(
    val manName: String,
    val modelName: String,
) : RepresentationModel<CarInfoPKResource>() {
    constructor(info: CarInfo) : this(
        manName = info.carInfoPk.manufacturer,
        modelName = info.carInfoPk.modelName,
    )
}
