package com.infinum.academy.cars.services

import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.exceptions.NoModelsException
import com.infinum.academy.cars.repository.CarInfoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CarInfoAdministrationService(
    private val service: CarInfoService,
    private val infoRepository: CarInfoRepository
) {
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

    fun getAllModelsInShop(pageable: Pageable): Page<CarInfo> {
        return infoRepository.findModelsWhichExistInShop(pageable)
    }
}