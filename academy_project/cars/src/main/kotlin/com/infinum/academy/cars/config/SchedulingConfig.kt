package com.infinum.academy.cars.config

import com.infinum.academy.cars.services.CarInfoService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class SchedulingConfig(
    private val service: CarInfoService
) {
    @Scheduled(fixedRate = 14_400_000)
    fun fetchCarInfoIntoDB() {
        service.saveModelsFromServer()
    }
}