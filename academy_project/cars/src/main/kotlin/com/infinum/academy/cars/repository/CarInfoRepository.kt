package com.infinum.academy.cars.repository

import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.domain.CarInfoPrimaryKey
import org.springframework.data.repository.Repository

interface CarInfoRepository : Repository<CarInfo, CarInfoPrimaryKey> {

    fun existsCarInfoByCarInfoPk(carInfoPrimaryKey: CarInfoPrimaryKey): Boolean

    fun save(carInfo: CarInfo): CarInfo

    fun findByCarInfoPk(carInfoPrimaryKey: CarInfoPrimaryKey): CarInfo?

}