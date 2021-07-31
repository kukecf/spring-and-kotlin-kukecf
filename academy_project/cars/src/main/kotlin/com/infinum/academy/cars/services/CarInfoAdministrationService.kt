package com.infinum.academy.cars.services

import com.infinum.academy.cars.exceptions.NoModelsException
import com.infinum.academy.cars.repository.CarInfoRepository
import org.springframework.stereotype.Service

@Service
class CarInfoAdministrationService(
    private val service: CarInfoService,
    private val infoRepository: CarInfoRepository
) {
    fun saveModelsFromServer() {
        service.getModelsFromServer()?.filter {
            infoRepository.existsCarInfoByCarInfoPk(
                it.carInfoPk
            ).not()
        }?.forEach {
            infoRepository.save(it)
        } ?: throw NoModelsException()
    }

    fun deleteModels() {
        infoRepository.deleteAll()
    }
}