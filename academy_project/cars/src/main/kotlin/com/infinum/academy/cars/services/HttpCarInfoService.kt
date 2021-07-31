package com.infinum.academy.cars.services

import com.infinum.academy.cars.domain.CarInfo
import com.infinum.academy.cars.domain.CarInfoPrimaryKey
import com.infinum.academy.cars.dto.CarInfoDto
import com.infinum.academy.cars.dto.CarResponse
import com.infinum.academy.cars.dto.toCarInfo
import com.infinum.academy.cars.exceptions.NoModelsException
import com.infinum.academy.cars.repository.CarInfoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
annotation class RestService

@RestService
class HttpCarInfoService(
    private val webClient: WebClient
) : CarInfoService{
    override fun getModelsFromServer(): List<CarInfo>? {
        return webClient
            .get()
            .uri("/api/v1/cars")
            .retrieve()
            .bodyToMono<CarResponse>()
            .map { response -> response.data.map{dto->dto.toCarInfo()} }
            .block()
    }
}

