package com.infinum.academy.cars.domain

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "carmodels")
data class CarInfo(

    @EmbeddedId
    val carInfoPk: CarInfoPrimaryKey,

    @Column(name = "isCommon")
    val isCommon: Boolean

)

@Embeddable
data class CarInfoPrimaryKey(
    val manufacturer: String,
    @Column(name = "modelName")
    val modelName: String
) : Serializable
