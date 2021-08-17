package com.infinum.academy.cars.resource

import com.infinum.academy.cars.domain.CarCheckUp
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
data class CheckUpResource(
    val id: Long = 0,
    val datePerformed: LocalDateTime,
    val workerName: String,
    val price: Float
) : RepresentationModel<CheckUpResource>() {
    constructor(checkup: CarCheckUp) : this(
        checkup.id,
        checkup.datePerformed,
        checkup.workerName,
        checkup.price,
    )
}
