package com.infinum.academy.cars.services

import com.infinum.academy.cars.domain.CarInfoPrimaryKey
import com.infinum.academy.cars.dto.CarInfoDto
import com.infinum.academy.cars.dto.CarResponse
import com.infinum.academy.cars.dto.toCarInfo
import com.infinum.academy.cars.exceptions.NoModelsException
import com.infinum.academy.cars.repository.CarInfoRepository
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
annotation class RestService

@RestService
class CarInfoService(
    private val webClient: WebClient,
    private val infoRepository: CarInfoRepository
) {
    fun getModelsFromServer(): List<CarInfoDto>? {
        return webClient
            .get()
            .uri("/api/v1/cars")
            .retrieve()
            .bodyToMono<CarResponse>()
            .map { response -> response.data }
            .block()
    }

    fun saveModelsFromServer() {
        getModelsFromServer()?.filter {
            infoRepository.existsCarInfoByCarInfoPk(CarInfoPrimaryKey(
                it.man_name,
                it.model_name
            )).not()
        }
            ?.forEach {
                infoRepository.save(it.toCarInfo())
            } ?: throw NoModelsException()
    }
}

