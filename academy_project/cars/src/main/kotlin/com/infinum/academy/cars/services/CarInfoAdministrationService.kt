package com.infinum.academy.cars.services

import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.domain.CarInfoPrimaryKey
import com.infinum.academy.cars.exceptions.NoModelsException
import com.infinum.academy.cars.repository.CarInfoRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CarInfoAdministrationService(
    private val service: CarInfoService,
    private val infoRepository: CarInfoRepository
) {
    @CacheEvict("model-info",allEntries=true)
    fun saveModelsFromServer() {
        infoRepository.saveAll(service.getModelsFromServer()?.filter {
            infoRepository.existsCarInfoByCarInfoPk(
                it.carInfoPk
            ).not()
        } ?: throw NoModelsException())
    }

    fun deleteModels() {
        infoRepository.deleteAll()
    }

    fun getModelWithId(manufacturer: String, model: String): CarInfo {
        return infoRepository.findByCarInfoPk(CarInfoPrimaryKey(manufacturer, model)) ?: throw NoModelsException()
    }
}
