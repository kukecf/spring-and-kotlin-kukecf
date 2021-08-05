package com.infinum.academy.cars.repository

import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.domain.CarInfoPrimaryKey
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface CarInfoRepository : Repository<CarInfo, CarInfoPrimaryKey> {

    fun existsCarInfoByCarInfoPk(carInfoPrimaryKey: CarInfoPrimaryKey): Boolean

    fun save(carInfo: CarInfo): CarInfo

    fun findByCarInfoPk(carInfoPrimaryKey: CarInfoPrimaryKey): CarInfo?

    @Query("select distinct model from CarInfo model join fetch Car car on car.info.carInfoPk = model.carInfoPk")
    fun findModelsWhichExistInShop(pageable: Pageable): Page<CarInfo>

    fun saveAll(carInfos: Iterable<CarInfo>): Iterable<CarInfo>

    fun deleteAll()
}
