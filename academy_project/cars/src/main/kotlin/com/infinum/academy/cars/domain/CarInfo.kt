package com.infinum.academy.cars.domain

import org.springframework.data.redis.core.RedisHash
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "carmodels")
//@RedisHash
data class CarInfo(

    @EmbeddedId
    val carInfoPk: CarInfoPrimaryKey,

    @Column(name = "is_common")
    val isCommon: Boolean

):Serializable

@Embeddable
data class CarInfoPrimaryKey(
    val manufacturer: String,
    @Column(name = "model_name")
    val modelName: String
) : Serializable
