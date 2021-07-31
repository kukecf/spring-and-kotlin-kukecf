package com.infinum.academy.cars.config

import com.infinum.academy.cars.services.CarInfoAdministrationService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
@Profile("!test")
class SchedulingConfig(
    private val service: CarInfoAdministrationService
) {
    @Scheduled(fixedRate = 86_400_000)
    fun fetchCarInfoIntoDB() {
        service.saveModelsFromServer()
    }
}