package com.infinum.academy.cars.services

import com.infinum.academy.cars.exceptions.NoModelsException
import com.infinum.academy.cars.repository.CarInfoRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class CarInfoAdministrationService(
    private val service: CarInfoService,
    private val infoRepository: CarInfoRepository
) {
    @CacheEvict("model-info", allEntries = true)
    fun saveModelsFromServer() {
        infoRepository.saveAll(service.getModelsFromServer()?.filter {
            infoRepository.existsCarInfoByCarInfoPk(
                it.carInfoPk
            ).not()
        } ?: throw NoModelsException())
    }
}
