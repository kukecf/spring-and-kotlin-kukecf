package com.infinum.academy.cars.repository

import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.domain.CarInfoPrimaryKey
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface CarInfoRepository : Repository<CarInfo, CarInfoPrimaryKey> {

    fun existsCarInfoByCarInfoPk(carInfoPrimaryKey: CarInfoPrimaryKey): Boolean

    fun save(carInfo: CarInfo): CarInfo

    @Cacheable("model-info")
    fun findByCarInfoPk(carInfoPrimaryKey: CarInfoPrimaryKey): CarInfo?

    fun saveAll(carInfos: Iterable<CarInfo>): Iterable<CarInfo>

    @CacheEvict("model-info",allEntries=true)
    fun deleteAll()
}
